package edu.byu.cs.tweeter.server.DAO.DynamoDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.DataPage;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.Follows;
import edu.byu.cs.tweeter.server.DAO.FollowDAO;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

/**
 * A DAO for accessing 'following/followers' data from a DynamoDB database.
 */
public class DynamoDBFollowDAO implements FollowDAO
{
    private static final String TableName = "follows";
    public static final String IndexName = "follows_index";
    private static final String FollowerAttr = "follower_handle";
    private static final String FolloweeAttr = "followee_handle";
    private final int batchSize = 25;
    private static final DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_WEST_2)
            .build();
    private static final DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();
    private final DynamoDbTable<Follows> table = enhancedClient
            .table(TableName, TableSchema.fromBean(Follows.class));
    private final DynamoDbIndex<Follows> index = enhancedClient
            .table(TableName, TableSchema.fromBean(Follows.class))
            .index(IndexName);

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }
    @Override
    public void putFollow(User follower, User followee)
    {
        Follows newFollow = new Follows();
        newFollow.setFollower_handle(follower.getAlias());
        newFollow.setFollower_name(follower.getName());
        newFollow.setFollower_imageURL(follower.getImageUrl());
        newFollow.setFollowee_handle(followee.getAlias());
        newFollow.setFollowee_name(followee.getName());
        newFollow.setFollowee_imageURL(followee.getImageUrl());
        table.putItem(newFollow);
    }
    @Override
    public void deleteFollow(User follower, User followee)
    {
        Key key = Key.builder()
                .partitionValue(follower.getAlias())
                .sortValue(followee.getAlias())
                .build();
        table.deleteItem(key);
    }

    @Override
    public void putFollowBatch(List<Follows> followsRows) {
        List<Follows> batchToWrite = new ArrayList<>();
        for (Follows followsRow : followsRows)
        {
            batchToWrite.add(followsRow);
            if(batchToWrite.size() == batchSize)
            {
                writeFollowBatch(batchToWrite);
                batchToWrite = new ArrayList<>();
            }
        }
        if (batchToWrite.size() > 0)
            writeFollowBatch(batchToWrite);
    }
    private void writeFollowBatch(List<Follows> followsRows)
    {
        if(followsRows.size() > 25)
            throw new RuntimeException("Too many rows to write");
        WriteBatch.Builder<Follows> writeBuilder = WriteBatch.builder(Follows.class).mappedTableResource(table);
        for (Follows followsRow : followsRows) {
            writeBuilder.addPutItem(builder -> builder.item(followsRow));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(table).size() > 0) {
                writeFollowBatch(result.unprocessedPutItemsForTable(table));
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
    @Override
    public void clearTable(List<Follows> followsRows)
    {
        List<Key> batchToDelete = new ArrayList<>();
        for (Follows followsRow : followsRows)
        {
            Key key = Key.builder()
                    .partitionValue(followsRow.getFollower_name())
                    .sortValue(followsRow.getFollowee_name())
                    .build();
            batchToDelete.add(key);
            if(batchToDelete.size() == batchSize)
            {
                deleteFollowBatch(batchToDelete);
                batchToDelete = new ArrayList<>();
            }
        }
        if (batchToDelete.size() > 0)
            deleteFollowBatch(batchToDelete);
    }

    private void deleteFollowBatch(List<Key> followsRows)
    {
        if(followsRows.size() > batchSize)
            throw new RuntimeException("Too many rows to delete");
        WriteBatch.Builder<Follows> writeBuilder = WriteBatch.builder(Follows.class).mappedTableResource(table);
        for (Key followsRow : followsRows) {
            writeBuilder.addDeleteItem(builder -> builder.key(followsRow));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get deleted this time
            if (result.unprocessedPutItemsForTable(table).size() > 0)
                deleteFollowBatch(result.unprocessedDeleteItemsForTable(table));

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
    @Override
    public Follows getFollow(String followerAlias, String followeeAlias)
    {
        Key key = Key.builder()
                .partitionValue(followerAlias)
                .sortValue(followeeAlias)
                .build();
        return table.getItem(key);
    }
    private interface GetFollowPageStrategy
    {
        Map<String, AttributeValue> getStartKey();
        PageIterable<Follows> getPages(QueryEnhancedRequest queryEnhancedRequest);
    }
    @Override
    public DataPage<Follows> getSecondaryFollowPage(String targetUserAlias, int limit, String lastItem)
    {
        GetFollowPageStrategy strategy = new GetFollowPageStrategy() {
            @Override
            public Map<String, AttributeValue> getStartKey()
            {
                Map<String, AttributeValue> startKey = new HashMap<>();
                startKey.put(FolloweeAttr, AttributeValue.builder()
                        .s(targetUserAlias).build());
                startKey.put(FollowerAttr, AttributeValue.builder()
                        .s(lastItem).build());
                return startKey;
            }
            @Override
            public PageIterable<Follows> getPages(QueryEnhancedRequest queryEnhancedRequest) {
                SdkIterable<Page<Follows>> sdkIterable = index.query(queryEnhancedRequest);
                return PageIterable.create(sdkIterable);
            }
        };
        return getFollowPage(targetUserAlias, limit, lastItem, strategy);
    }
    @Override
    public DataPage<Follows> getFollowPage(String targetUserAlias, int limit, String lastItem)
    {
        GetFollowPageStrategy strategy = new GetFollowPageStrategy() {
            @Override
            public Map<String, AttributeValue> getStartKey() {
                Map<String, AttributeValue> startKey = new HashMap<>();
                startKey.put(FollowerAttr, AttributeValue.builder()
                        .s(targetUserAlias).build());
                startKey.put(FolloweeAttr, AttributeValue.builder()
                        .s(lastItem).build());
                return startKey;
            }
            @Override
            public PageIterable<Follows> getPages(QueryEnhancedRequest queryEnhancedRequest) {
                return table.query(queryEnhancedRequest);
            }
        };
        return getFollowPage(targetUserAlias, limit, lastItem, strategy);
    }
    private DataPage<Follows> getFollowPage(String targetUserAlias, int limit, String lastItem, GetFollowPageStrategy strategy)
    {
            Key key = Key.builder()
                    .partitionValue(targetUserAlias)
                    .build();

            QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                    .queryConditional(QueryConditional.keyEqualTo(key))
                    .limit(limit);

            if(isNonEmptyString(lastItem))
                requestBuilder.exclusiveStartKey(strategy.getStartKey());

            QueryEnhancedRequest queryEnhancedRequest = requestBuilder.build();
            PageIterable<Follows> pages = strategy.getPages(queryEnhancedRequest);
            DataPage<Follows> result = new DataPage<Follows>();
            pages.stream()
                    .limit(1)
                    .forEach((Page<Follows> page) ->
                    {
                        result.setHasMorePages(page.lastEvaluatedKey() != null);
                        page.items().forEach(follow ->
                        {
                            result.getValues().add(follow);
                        });
                    });
            return result;
    }
}
