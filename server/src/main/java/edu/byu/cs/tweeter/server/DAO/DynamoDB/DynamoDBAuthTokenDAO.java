package edu.byu.cs.tweeter.server.DAO.DynamoDB;

import edu.byu.cs.tweeter.server.DAO.AuthTokenDAO;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.AuthTokens;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoDBAuthTokenDAO implements AuthTokenDAO
{
    private static final String AuthTokensTableName = "authTokens";
    private static final DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_WEST_2)
            .build();
    private static final DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();
    private final DynamoDbTable<AuthTokens> authTokensTable = enhancedClient
            .table(AuthTokensTableName, TableSchema.fromBean(AuthTokens.class));
    @Override
    public AuthTokens getAuthToken(String token)
    {
        Key key = Key.builder()
                .partitionValue(token)
                .build();
        return authTokensTable.getItem(key);
    }
    @Override
    public void updateAuthTokensTable(Long threshold)
    {
        boolean done = false;
        Key key = Key.builder()
                .partitionValue(threshold)
                .build();
        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.sortLessThanOrEqualTo(key))
                .limit(25);
        while (!done)
        {
            QueryEnhancedRequest queryEnhancedRequest = requestBuilder.build();
            PageIterable<AuthTokens> pages = authTokensTable.query(queryEnhancedRequest);
            pages.stream()
                    .limit(1)
                    .forEach((Page<AuthTokens> page) ->
                            page.items()
                                    .forEach(authTokensRow ->
                                    {
                                        Key newKey = Key.builder()
                                                .partitionValue(authTokensRow.getAuthToken())
                                                .build();
                                        authTokensTable.deleteItem(newKey);
                                    }));
            if (pages.stream().count() < 25)
                done = true;
        }
    }
    @Override
    public void deleteAuthToken(String token)
    {
        Key key = Key.builder()
                .partitionValue(token)
                .build();
        authTokensTable.deleteItem(key);
    }
    @Override
    public AuthTokens putAuthToken(String token, String alias) {
        AuthTokens authTokenRow = new AuthTokens();
        authTokenRow.setAuthToken(token);
        authTokenRow.setUsername(alias);
        authTokenRow.setDateTime(System.currentTimeMillis());
        authTokensTable.putItem(authTokenRow);
        return authTokenRow;
    }
}
