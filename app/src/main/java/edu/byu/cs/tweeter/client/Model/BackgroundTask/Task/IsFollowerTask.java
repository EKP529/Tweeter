package edu.byu.cs.tweeter.client.Model.BackgroundTask.Task;

import android.os.Bundle;
import android.os.Handler;

import java.util.Random;

import edu.byu.cs.tweeter.client.Model.Net.ServerFacade;
import edu.byu.cs.tweeter.client.Model.Service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.GetCountResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;

/**
 * Background task that determines if one user is following another.
 */
public class IsFollowerTask extends AuthenticatedTask
{
    public static final String IS_FOLLOWER_KEY = "is-follower";

    /**
     * The alleged follower.
     */
    private final User follower;

    /**
     * The alleged followee.
     */
    private final User followee;
    private boolean isFollower;
    private ServerFacade serverFacade;

    public IsFollowerTask(AuthToken authToken, User follower, User followee, Handler messageHandler) {
        super(authToken, messageHandler);
        this.follower = follower;
        this.followee = followee;
    }

    @Override
    protected void runTask() throws Exception
    {
        String followerAlias = getFollower() == null ? null : getFollower().getAlias();
        String followeeAlias = getFollowee() == null ? null : getFollowee().getAlias();
        IsFollowerRequest request = new IsFollowerRequest(getAuthToken(), followerAlias, followeeAlias);
        IsFollowerResponse response = getServerFacade().isFollower(request, FollowService.IS_FOLLOWER_URL_PATH);
        if (response.isSuccess())
        {
            isFollower = response.isFollower();
            sendSuccessMessage();
        }
        else
        {
            sendFailedMessage(response.getMessage());
        }
    }
    private User getFollower() { return follower; }
    private User getFollowee() { return followee; }
    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putBoolean(IS_FOLLOWER_KEY, isFollower);
    }

    protected ServerFacade getServerFacade()
    {
        if(serverFacade == null)
        {
            serverFacade = new ServerFacade();
        }
        return serverFacade;
    }
}
