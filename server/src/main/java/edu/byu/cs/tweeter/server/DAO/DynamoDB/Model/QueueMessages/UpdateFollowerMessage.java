package edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.QueueMessages;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class UpdateFollowerMessage
{
    Status postedStatus;
    List<String> followers;

    public UpdateFollowerMessage() {}
    public UpdateFollowerMessage(Status postedStatus, List<String> followers) {
        this.postedStatus = postedStatus;
        this.followers = followers;
    }

    public Status getPostedStatus() {
        return postedStatus;
    }

    public void setPostedStatus(Status postedStatus) {
        this.postedStatus = postedStatus;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }
}
