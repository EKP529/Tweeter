package edu.byu.cs.tweeter.client.Model.BackgroundTask.Task;

import android.os.Handler;

import edu.byu.cs.tweeter.client.Model.Service.UserService;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticateTask
{
    public LoginTask(String username, String password, Handler messageHandler)
    {
        super(messageHandler, username, password);
    }

    @Override
    protected AuthenticateResponse runAuthenticationTask() throws Exception
    {
        LoginRequest request = new LoginRequest(username, password);
        return getServerFacade().login(request, UserService.LOGIN_URL_PATH);
    }
}
