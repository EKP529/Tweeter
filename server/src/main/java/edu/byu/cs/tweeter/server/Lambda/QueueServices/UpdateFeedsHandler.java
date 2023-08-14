package edu.byu.cs.tweeter.server.Lambda.QueueServices;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import edu.byu.cs.tweeter.model.net.JsonSerializer;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.QueueMessages.UpdateFollowerMessage;
import edu.byu.cs.tweeter.server.DAO.StatusDAO;
import edu.byu.cs.tweeter.server.Lambda.DynamoDBHandler;

public class UpdateFeedsHandler extends DynamoDBHandler implements RequestHandler<SQSEvent, Void>
{

    @Override
    public Void handleRequest(SQSEvent event, Context context)
    {
        for (SQSEvent.SQSMessage msg : event.getRecords())
        {
            UpdateFollowerMessage updateFollowerMessage = JsonSerializer.deserialize(msg.getBody(), UpdateFollowerMessage.class);
            getStatusDAO().updateFeedsTable(updateFollowerMessage.getFollowers(), updateFollowerMessage.getPostedStatus());
        }
        return null;
    }
    public StatusDAO getStatusDAO()
    {
        return getFactory().getStatusDAO();
    }
}
