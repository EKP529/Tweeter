package edu.byu.cs.tweeter.server.DAO.DynamoDB;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.Users;
import edu.byu.cs.tweeter.server.DAO.UserDAO;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

public class DynamoDBUserDAO implements UserDAO
{
    private static final String UsersTableName = "users";
    private final int batchSize = 25;
    private static final DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_WEST_2)
            .build();
    private static final DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();
    private final DynamoDbTable<Users> usersTable = enhancedClient
            .table(UsersTableName, TableSchema.fromBean(Users.class));

    @Override
    public Users getUser(String alias)
    {
        Key key = Key.builder()
                .partitionValue(alias)
                .build();
        return usersTable.getItem(key);
    }
    @Override
    public Users putUser(User newUser, String passwordHash)
    {
        Key key = Key.builder()
                .partitionValue(newUser.getAlias())
                .build();
        Users usersRow = usersTable.getItem(key);
        if (usersRow != null)
            throw new RuntimeException("[Bad Request] Username already in use");
        usersRow = new Users();
        usersRow.setPasswordHash(passwordHash);
        usersRow.setName(newUser.getFirstName() + " " + newUser.getLastName());
        usersRow.setHandle(newUser.getAlias());
        usersRow.setImageURL(getImageURLFromS3Bucket(newUser.getImageUrl(), newUser.getAlias()));
        usersRow.setFollowersCount(0);
        usersRow.setFollowingCount(0);
        usersTable.putItem(usersRow);
        return usersRow;
    }

    @Override
    public void updateFollowersCount(String alias, char type)
    {
        Key key = Key.builder()
                .partitionValue(alias)
                .build();
        Users usersRow = usersTable.getItem(key);
        switch(type)
        {
            case '+':
                usersRow.incrementFollowersCount();
                break;
            case '-':
                usersRow.decrementFollowersCount();
                break;
            default:
                throw new RuntimeException("[Bad Request] Bad parameter");
        }
        usersTable.updateItem(usersRow);
    }
    @Override
    public void updateFollowingCount(String alias, char type) {
        Key key = Key.builder()
                .partitionValue(alias)
                .build();
        Users usersRow = usersTable.getItem(key);
        switch(type)
        {
            case '+':
                usersRow.incrementFollowingCount();
                break;
            case '-':
                usersRow.decrementFollowingCount();
                break;
            default:
                throw new RuntimeException("[Bad Request] Bad parameter");
        }
        usersTable.updateItem(usersRow);
    }

    @Override
    public void putUserBatch(List<Users> usersRows)
    {
        List<Users> batchToWrite = new ArrayList<>();
        for (Users usersRow : usersRows)
        {
            batchToWrite.add(usersRow);
            if(batchToWrite.size() == 25)
            {
                writeUserBatch(batchToWrite);
                batchToWrite = new ArrayList<>();
            }
        }
        if (batchToWrite.size() > 0)
            writeUserBatch(batchToWrite);
    }

    private void writeUserBatch(List<Users> usersRows)
    {
        if(usersRows.size() > 25)
            throw new RuntimeException("Too many users to write");
        WriteBatch.Builder<Users> writeBuilder = WriteBatch.builder(Users.class).mappedTableResource(usersTable);
        for (Users usersRow : usersRows) {
            writeBuilder.addPutItem(builder -> builder.item(usersRow));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(usersTable).size() > 0) {
                writeUserBatch(result.unprocessedPutItemsForTable(usersTable));
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void clearTable(List<Users> usersRows) {
        List<Key> batchToDelete = new ArrayList<>();
        for (Users usersRow : usersRows)
        {
            Key key = Key.builder()
                    .partitionValue(usersRow.getHandle())
                    .build();
            batchToDelete.add(key);
            if(batchToDelete.size() == batchSize)
            {
                deleteUserBatch(batchToDelete);
                batchToDelete = new ArrayList<>();
            }
        }
        if (batchToDelete.size() > 0)
            deleteUserBatch(batchToDelete);
    }
    private void deleteUserBatch(List<Key> usersRows)
    {
        if(usersRows.size() > batchSize)
            throw new RuntimeException("Too many rows to delete");
        WriteBatch.Builder<Users> writeBuilder = WriteBatch.builder(Users.class).mappedTableResource(usersTable);
        for (Key usersRow : usersRows) {
            writeBuilder.addDeleteItem(builder -> builder.key(usersRow));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get deleted this time
            if (result.unprocessedPutItemsForTable(usersTable).size() > 0)
                deleteUserBatch(result.unprocessedDeleteItemsForTable(usersTable));

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private String getImageURLFromS3Bucket(String image, String alias)
    {
        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion("us-west-2")
                .build();
        byte[] byteArray = Base64.getDecoder().decode(image);
        ObjectMetadata data = new ObjectMetadata();
        data.setContentLength(byteArray.length);
        data.setContentType("image/jpeg");
        PutObjectRequest request = new PutObjectRequest(
                "ekptweeterprofileimages", alias,
                new ByteArrayInputStream(byteArray), data)
                .withCannedAcl(CannedAccessControlList.PublicRead);
        s3.putObject(request);
        return "https://ekptweeterprofileimages.s3.us-west-2.amazonaws.com/" + alias;
    }

}
