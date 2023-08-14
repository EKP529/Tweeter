package edu.byu.cs.tweeter.server.Service;

import java.util.ArrayList;
import java.util.List;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.JsonSerializer;
import edu.byu.cs.tweeter.model.net.request.PagedStatusRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PagedStatusResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.DAO.DAOFactory;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.DataPage;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.Feeds;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.Follows;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.QueueMessages.PostStatusMessage;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.Stories;
import edu.byu.cs.tweeter.server.DAO.FollowDAO;
import edu.byu.cs.tweeter.server.DAO.StatusDAO;

/**
 * Contains the business logic for getting the statuses of a user's story and feed.
 */
public class StatusService extends Service
{
    String postStatusQueueURL = "https://sqs.us-west-2.amazonaws.com/070541511803/PostStatusQueue";

    public StatusService(DAOFactory factory)
    {
        super(factory);
    }
    public PostStatusResponse postStatus(PostStatusRequest request)
    {
        if (request.getStatus() == null)
            throw new RuntimeException("[Bad Request] Request needs to have a status");
        if (validAuthToken(request.getAuthToken()))
        {
            getStatusDAO().putStatusToStory(request.getStatus());
            writePostStatusMessage(request.getStatus());
            return new PostStatusResponse();
        }
        return new PostStatusResponse("Authentication Failed");

    }
    private interface GetStatusesStrategy<T>
    {
        DataPage<T> getPage();
    }
    public PagedStatusResponse getFeed(PagedStatusRequest request)
    {
        GetStatusesStrategy<Feeds> strategy = () ->
                getStatusDAO().getFeed(
                        request.getTargetUserAlias(),
                        request.getLimit(),
                        request.getLastItem());
        return getStatuses(request, strategy);
    }
    public PagedStatusResponse getStory(PagedStatusRequest request)
    {
        GetStatusesStrategy<Stories> strategy = () ->
                getStatusDAO().getStory(
                        request.getTargetUserAlias(),
                        request.getLimit(),
                        request.getLastItem());
        return getStatuses(request, strategy);
    }
    public <T extends Stories> PagedStatusResponse getStatuses(PagedStatusRequest request, GetStatusesStrategy<T> strategy)
    {
        if(request.getTargetUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        if (validAuthToken(request.getAuthToken()))
        {
            DataPage<T> page = strategy.getPage();
            PagedStatusResponse response =
                    new PagedStatusResponse(new ArrayList<>(request.getLimit()), page.isHasMorePages());
            for (Stories tableRow : page.getValues())
            {
                String alias = tableRow.getWriter_handle();
                String imageURL = tableRow.getProfilePicURL();
                String[] name = tableRow.getWriter_name().split(" ");
                User user = new User(name[0], name[1], alias, imageURL);
                String post = tableRow.getPost();
                Long timestamp = tableRow.getTimestamp();
                List<String> urls = tableRow.getUrls();
                List<String> mentions = tableRow.getMentions();
                Status status = new Status(post, user, timestamp, urls, mentions);
                response.getItems().add(status);
            }
            return response;
        }
        return new PagedStatusResponse("Authentication Failed: Logout to restart");
    }
    public void writePostStatusMessage(Status status)
    {
        PostStatusMessage message = new PostStatusMessage(status);
        String messageBody = JsonSerializer.serialize(message);

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(postStatusQueueURL)
                .withMessageBody(messageBody);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        sqs.sendMessage(send_msg_request);
    }
    public StatusDAO getStatusDAO() {
        return getFactory().getStatusDAO();
    }
    public FollowDAO getFollowDAO() {
        return getFactory().getFollowDAO();
    }
}
