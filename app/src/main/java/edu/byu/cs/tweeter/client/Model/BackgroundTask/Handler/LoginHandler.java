package edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.LoginTask;
import edu.byu.cs.tweeter.client.Model.Observer.StateObserver;
import edu.byu.cs.tweeter.client.Model.Cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Message handler (i.e., observer) for LoginTask
 */
public class LoginHandler extends BackgroundTaskHandler<StateObserver<User>>
{
    public LoginHandler(StateObserver<User> observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(StateObserver<User> observer, Bundle data) {
        User loggedInUser = (User) data.getSerializable(LoginTask.USER_KEY);
        AuthToken authToken = (AuthToken) data.getSerializable(LoginTask.AUTH_TOKEN_KEY);
        // Cache user session information
        Cache.getInstance().setCurrUser(loggedInUser);
        Cache.getInstance().setCurrUserAuthToken(authToken);

        observer.handleSuccess(loggedInUser);
    }
}
