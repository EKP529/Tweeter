package edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.RegisterTask;
import edu.byu.cs.tweeter.client.Model.Observer.StateObserver;
import edu.byu.cs.tweeter.client.Model.Cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterHandler extends BackgroundTaskHandler<StateObserver<User>>
{
    public RegisterHandler(StateObserver<User> observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(StateObserver<User> observer, Bundle data)
    {
        User registeredUser = (User) data.getSerializable(RegisterTask.USER_KEY);
        AuthToken authToken = (AuthToken) data.getSerializable(RegisterTask.AUTH_TOKEN_KEY);
        Cache.getInstance().setCurrUser(registeredUser);
        Cache.getInstance().setCurrUserAuthToken(authToken);
        observer.handleSuccess(registeredUser);
    }
}
