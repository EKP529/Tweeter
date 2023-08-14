package edu.byu.cs.tweeter.client.Model.BackgroundTask.Task;

import android.os.Handler;
import edu.byu.cs.tweeter.client.Model.Net.ServerFacade;
import edu.byu.cs.tweeter.client.Model.Service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;

/**
 * Background task that logs out a user (i.e., ends a session).
 */
public class LogoutTask extends AuthenticatedTask {

    private ServerFacade serverFacade;

    public LogoutTask(AuthToken authToken, Handler messageHandler) {
        super(authToken, messageHandler);
    }

    @Override
    protected void runTask() throws Exception
    {
        LogoutRequest request = new LogoutRequest(getAuthToken());
        LogoutResponse response = getServerFacade().logout(request, UserService.LOGOUT_URL_PATH);
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
