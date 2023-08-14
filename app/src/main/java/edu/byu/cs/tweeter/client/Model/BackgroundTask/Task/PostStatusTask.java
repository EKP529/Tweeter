package edu.byu.cs.tweeter.client.Model.BackgroundTask.Task;

import android.os.Handler;

import edu.byu.cs.tweeter.client.Model.Net.ServerFacade;
import edu.byu.cs.tweeter.client.Model.Service.StatusService;
import edu.byu.cs.tweeter.client.Model.Service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

/**
 * Background task that posts a new status sent by a user.
 */
public class PostStatusTask extends AuthenticatedTask
{

    /**
     * The new status being sent. Contains all properties of the status,
     * including the identity of the user sending the status.
     */
    private final Status status;
    private ServerFacade serverFacade;

    public PostStatusTask(AuthToken authToken, Status status, Handler messageHandler) {
        super(authToken, messageHandler);
        this.status = status;
    }

    @Override
    protected void runTask() throws Exception
    {
        PostStatusRequest request = new PostStatusRequest(getAuthToken(), status);
        PostStatusResponse response = getServerFacade().postStatus(request, StatusService.POST_STATUS_URL_PATH);
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
