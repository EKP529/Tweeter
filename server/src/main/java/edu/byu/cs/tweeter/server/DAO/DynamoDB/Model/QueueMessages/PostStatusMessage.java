package edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.QueueMessages;

import edu.byu.cs.tweeter.model.domain.Status;

public class PostStatusMessage
{
    Status postedStatus;

    public PostStatusMessage() {}

    public PostStatusMessage(Status postedStatus) {
        this.postedStatus = postedStatus;
    }

    public Status getPostedStatus() {
        return postedStatus;
    }

    public void setPostedStatus(Status postedStatus) {
        this.postedStatus = postedStatus;
    }
}
