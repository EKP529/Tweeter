package edu.byu.cs.tweeter.client.Model.BackgroundTask.Task;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.client.Model.Net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;

public abstract class AuthenticateTask extends BackgroundTask
{

    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";

    private User authenticatedUser;

    private AuthToken authToken;

    /**
     * The user's username (or "alias" or "handle"). E.g., "@susan".
     */
    protected final String username;

    /**
     * The user's password.
     */
    protected final String password;
    private ServerFacade serverFacade;

    protected AuthenticateTask(Handler messageHandler, String username, String password) {
        super(messageHandler);
        this.username = username;
        this.password = password;
    }


    @Override
    protected final void runTask() throws Exception
    {
        AuthenticateResponse response = runAuthenticationTask();
        if (response.isSuccess())
        {
            authenticatedUser = response.getUser();
            authToken = response.getAuthToken();
            sendSuccessMessage();
        }
        else
        {
            sendFailedMessage(response.getMessage());
        }
    }

    protected abstract AuthenticateResponse runAuthenticationTask() throws Exception;

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, authenticatedUser);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, authToken);
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
            serverFacade = new ServerFacade();
        return serverFacade;
    }
}
