package edu.byu.cs.tweeter.server.DAO.DynamoDB.Model;

import edu.byu.cs.tweeter.server.DAO.DynamoDB.DynamoDBFollowDAO;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class Follows
{
    private String follower_handle;
    private String follower_name;
    private String follower_imageURL;
    private String followee_name;
    private String followee_handle;
    private String followee_imageURL;

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = DynamoDBFollowDAO.IndexName)
    public String getFollower_handle() {
        return follower_handle;
    }

    public void setFollower_handle(String follower_handle) {
        this.follower_handle = follower_handle;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = DynamoDBFollowDAO.IndexName)
    public String getFollowee_handle() {
        return followee_handle;
    }

    public void setFollowee_handle(String followee_handle) {
        this.followee_handle = followee_handle;
    }

    public String getFollower_name() {
        return follower_name;
    }

    public void setFollower_name(String follower_name) {
        this.follower_name = follower_name;
    }

    public String getFollowee_name() {
        return followee_name;
    }

    public void setFollowee_name(String followee_name) {
        this.followee_name = followee_name;
    }

    public String getFollower_imageURL() {
        return follower_imageURL;
    }

    public void setFollower_imageURL(String follower_imageURL) {
        this.follower_imageURL = follower_imageURL;
    }

    public String getFollowee_imageURL() {
        return followee_imageURL;
    }

    public void setFollowee_imageURL(String followee_imageURL) {
        this.followee_imageURL = followee_imageURL;
    }

//    @Override
//    public String toString() {
//        return "Follow{" +
//                "follower_handle='" + follower_handle + '\'' +
//                ", followee_handle='" + followee_handle + '\'' +
//                '}';
//    }
}
