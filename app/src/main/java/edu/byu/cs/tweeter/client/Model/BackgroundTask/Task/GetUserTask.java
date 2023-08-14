package edu.byu.cs.tweeter.client.Model.BackgroundTask.Task;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.client.Model.Net.ServerFacade;
import edu.byu.cs.tweeter.client.Model.Service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;

/**
 * Background task that returns the profile for a specified user.
 */
public class GetUserTask extends AuthenticatedTask
{
    public static final String USER_KEY = "user";

    /**
     * Alias (or handle) for user whose profile is being retrieved.
     */
    private final String alias;
    private User user;
    private ServerFacade serverFacade;

    public GetUserTask(AuthToken authToken, String alias, Handler messageHandler) {
        super(authToken, messageHandler);
        this.alias = alias;
    }

    @Override
    protected void runTask() throws Exception
    {
        GetUserRequest request = new GetUserRequest(getAuthToken(), alias);
        GetUserResponse response = getServerFacade().getUser(request, UserService.GET_USER_URL_PATH);
        if (response.isSuccess())
        {
            user = response.getUser();
            sendSuccessMessage();
        }
        else
        {
            sendFailedMessage(response.getMessage());
        }
    }
    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, user);
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
