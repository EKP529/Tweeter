package edu.byu.cs.tweeter.server.DAO.DynamoDB.Model;

import java.util.List;

import edu.byu.cs.tweeter.server.DAO.DynamoDB.DynamoDBStatusDAO;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class Stories
{
    private String writer_handle;
    private String writer_name;
    private String profilePicURL;
    private Long timestamp;
    private String post;
    private List<String> urls;
    private List<String> mentions;

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = DynamoDBStatusDAO.StoriesIndexName)
    public String getWriter_handle() {
        return writer_handle;
    }

    public void setWriter_handle(String writer_handle) {
        this.writer_handle = writer_handle;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = DynamoDBStatusDAO.StoriesIndexName)
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getMentions() {
        return mentions;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }

    public String getWriter_name() {
        return writer_name;
    }

    public void setWriter_name(String write_name) {
        this.writer_name = write_name;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }
}
