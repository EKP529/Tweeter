package edu.byu.cs.tweeter.server.DAO.DynamoDB.Model;

import java.util.List;

import edu.byu.cs.tweeter.server.DAO.DynamoDB.DynamoDBStatusDAO;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class Feeds extends Stories
{
    private String follower_handle;
    private String writer_handle;
    private Long timestamp;

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = DynamoDBStatusDAO.FeedsIndexName)
    public String getFollower_handle() {
        return follower_handle;
    }
    public void setFollower_handle(String follower_handle) {
        this.follower_handle = follower_handle;
    }
    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = DynamoDBStatusDAO.FeedsIndexName)
    public Long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    public String getWriter_handle() {
        return writer_handle;
    }
    public void setWriter_handle(String writer_handle) {
        this.writer_handle = writer_handle;
    }

}
