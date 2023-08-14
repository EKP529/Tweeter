package edu.byu.cs.tweeter.server.DAO.DynamoDB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.DataPage;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.Feeds;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.Follows;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.Stories;
import edu.byu.cs.tweeter.server.DAO.StatusDAO;
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
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;


/**
* A DAO for accessing 'story/feed' data from the database.
*/
public class DynamoDBStatusDAO implements StatusDAO
{
    private static final String StoriesTableName = "stories";
    public static final String StoriesIndexName = "stories_index";
    private static final String FeedsTableName = "feeds";
    public static final String FeedsIndexName = "feeds_index";
    private static final String WriterAttr = "writer_handle";
    private static final String FollowerAttr = "follower_handle";
    private static final String TimestampAttr = "timestamp";
    private static final DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_WEST_2)
            .build();
    private static final DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();
    private final DynamoDbTable<Stories> storiesTable = enhancedClient
            .table(StoriesTableName, TableSchema.fromBean(Stories.class));
//    private final DynamoDbIndex<Stories> storiesIndex = enhancedClient
//            .table(StoriesTableName, TableSchema.fromBean(Stories.class))
//            .index(StoriesIndexName);
    private final DynamoDbTable<Feeds> feedsTable = enhancedClient
            .table(FeedsTableName, TableSchema.fromBean(Feeds.class));
//    private final DynamoDbIndex<Feeds> feedsIndex = enhancedClient
//            .table(FeedsTableName, TableSchema.fromBean(Feeds.class))
//            .index(FeedsIndexName);

    private static boolean isNonEmptyNum(Long value) {
        return (value != null && value > 0);
    }
    @Override
    public void putStatusToStory(Status status)
    {
        Stories storiesRow = new Stories();
        storiesRow.setWriter_handle(status.getUser().getAlias());
        storiesRow.setWriter_name(status.getUser().getName());
        storiesRow.setProfilePicURL(status.getUser().getImageUrl());
        storiesRow.setPost(status.getPost());
        storiesRow.setTimestamp(status.getTimestamp());
        storiesRow.setMentions(status.getMentions());
        storiesRow.setUrls(status.getUrls());
        storiesTable.putItem(storiesRow);
    }
    private interface GetStatusesStrategy<T extends Stories>
    {
        String getAttribute();
        DynamoDbTable<T> getTable();
    }
    @Override
    public DataPage<Stories> getStory(String targetUserAlias, int limit, Long lastItem)
    {
        GetStatusesStrategy<Stories> strategy = new GetStatusesStrategy<>() {
            @Override
            public String getAttribute() {
                return WriterAttr;
            }
            @Override
            public DynamoDbTable<Stories> getTable() {
                return storiesTable;
            }
        };
        return getStatuses(targetUserAlias, limit, lastItem, strategy);
    }
    @Override
    public DataPage<Feeds> getFeed(String targetUserAlias, int limit, Long lastItem)
    {
        GetStatusesStrategy<Feeds> strategy = new GetStatusesStrategy<>() {
            @Override
            public String getAttribute() {
                return FollowerAttr;
            }
            @Override
            public DynamoDbTable<Feeds> getTable() {
                return feedsTable;
            }
        };
        return getStatuses(targetUserAlias, limit, lastItem, strategy);
    }
    public <T extends Stories> DataPage<T> getStatuses(String targetUserAlias, int limit, Long lastItem, GetStatusesStrategy<T> strategy)
    {
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit)
                .scanIndexForward(false);

        if(isNonEmptyNum(lastItem))
        {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(strategy.getAttribute(), AttributeValue.builder()
                    .s(targetUserAlias).build());
            startKey.put(TimestampAttr, AttributeValue.builder()
                    .n(lastItem.toString()).build());
            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest queryEnhancedRequest = requestBuilder.build();
        PageIterable<T> pages = strategy.getTable().query(queryEnhancedRequest);
        DataPage<T> result = new DataPage<>();
        pages.stream()
                .limit(1)
                .forEach((Page<T> page) ->
                {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(tableRow -> result.getValues().add(tableRow));
                });
        return result;
    }
    @Override
    public void updateFeedsTable(List<String> followers, Status status)
    {
        for (String follower : followers)
        {
            putStatusToFeed(status, follower);
        }
    }
    public void putStatusToFeed(Status status, String follower_handle)
    {
        Feeds feedsRow = new Feeds();
        feedsRow.setFollower_handle(follower_handle);
        feedsRow.setWriter_handle(status.getUser().getAlias());
        feedsRow.setWriter_name(status.getUser().getName());
        feedsRow.setProfilePicURL(status.getUser().getImageUrl());
        feedsRow.setPost(status.getPost());
        feedsRow.setTimestamp(status.getTimestamp());
        feedsRow.setMentions(status.getMentions());
        feedsRow.setUrls(status.getUrls());
        feedsTable.putItem(feedsRow);
    }
}
