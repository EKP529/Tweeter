package edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.GetUserTask;
import edu.byu.cs.tweeter.client.Model.Observer.StateObserver;
import edu.byu.cs.tweeter.client.Model.Service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Message handler (i.e., observer) for GetUserTask.
 */
public class GetUserHandler extends BackgroundTaskHandler<StateObserver<User>>
{
    public GetUserHandler(StateObserver<User> observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(StateObserver<User> observer, Bundle data) {
        User followee = (User) data.getSerializable(GetUserTask.USER_KEY);
        observer.handleSuccess(followee);
    }
}
