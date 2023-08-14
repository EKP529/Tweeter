package edu.byu.cs.tweeter.server.Lambda.QueueServices;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.JsonSerializer;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.DataPage;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.Follows;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.QueueMessages.PostStatusMessage;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.QueueMessages.UpdateFollowerMessage;
import edu.byu.cs.tweeter.server.DAO.FollowDAO;
import edu.byu.cs.tweeter.server.Lambda.DynamoDBHandler;

public class PostUpdateFeedMessagesHandler extends DynamoDBHandler implements RequestHandler<SQSEvent, Void>
{
    int pageSize = 25;
    String updateFeedQueueURL = "https://sqs.us-west-2.amazonaws.com/070541511803/UpdateFeedQueue";

    @Override
    public Void handleRequest(SQSEvent event, Context context)
    {
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            PostStatusMessage postStatusMessage = JsonSerializer.deserialize(msg.getBody(), PostStatusMessage.class);
            boolean done = false;
            String lastUserAlias = null;
            while (!done)
            {
                DataPage<Follows> page = getFollowDAO().getSecondaryFollowPage(postStatusMessage.getPostedStatus().getUser().getAlias(),
                        pageSize, lastUserAlias);
                List<String> followers = new ArrayList<>();
                for (Follows row : page.getValues())
                    followers.add(row.getFollower_handle());
                writeUpdateFollowerMessage(postStatusMessage.getPostedStatus(), followers);
                if (followers.size() < pageSize)
                    done = true;
                else
                    lastUserAlias = followers.get(followers.size() - 1);
            }
        }
        return null;
    }
    public void writeUpdateFollowerMessage(Status status, List<String> followers)
    {
        UpdateFollowerMessage updateFollowerMessage = new UpdateFollowerMessage(status, followers);
        String messageBody = JsonSerializer.serialize(updateFollowerMessage);

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(updateFeedQueueURL)
                .withMessageBody(messageBody);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        sqs.sendMessage(send_msg_request);
    }
    public FollowDAO getFollowDAO()
    {
        return getFactory().getFollowDAO();
    }
}
