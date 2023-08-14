package edu.byu.cs.tweeter.server.DAO.DynamoDB.Model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class Users
{
    private String handle;
    private String passwordHash;
    private String name;
    private String imageURL;
    private int followingCount;
    private int followersCount;

    @DynamoDbPartitionKey
    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle)
    {
        this.handle = handle;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getFollowingCount() {
        return followingCount;
    }
    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public void incrementFollowingCount() { this.followingCount++; }

    public void decrementFollowingCount() { this.followingCount--; }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public void incrementFollowersCount() { this.followersCount++; }

    public void decrementFollowersCount() { this.followersCount--; }
}
