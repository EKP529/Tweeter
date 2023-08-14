package edu.byu.cs.tweeter.client.Model.BackgroundTask.Task;

import android.os.Handler;
import edu.byu.cs.tweeter.client.Model.Net.ServerFacade;
import edu.byu.cs.tweeter.client.Model.Service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;

/**
 * Background task that removes a following relationship between two users.
 */
public class UnfollowTask extends AuthenticatedTask {

    /**
     * The user that is being followed.
     */
    private final User followee;
    private final User follower;
    private ServerFacade serverFacade;

    public UnfollowTask(AuthToken authToken, User follower,User followee, Handler messageHandler) {
        super(authToken, messageHandler);
        this.follower = follower;
        this.followee = followee;
    }

    @Override
    protected void runTask() throws Exception
    {
        UnfollowRequest request = new UnfollowRequest(getAuthToken(), follower, followee);
        UnfollowResponse response = getServerFacade().unfollow(request, FollowService.UNFOLLOW_URL_PATH);
        if (response.isSuccess())
        {
            sendSuccessMessage();
        }
        else
        {
            sendFailedMessage(response.getMessage());
        }
    }

    /**
     * Returns an instance of {@link ServerFacade}. Allows mocking of the ServerFacade class for
     * testing purposes. All usages of ServerFacade should get their instance from this method to
     * allow for proper mocking.
     *
     * @return the instance.
     */
    protected ServerFacade getServerFacade()
    {
        if(serverFacade == null)
        {
            serverFacade = new ServerFacade();
        }
        return serverFacade;
    }
}
