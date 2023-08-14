package edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.IsFollowerTask;
import edu.byu.cs.tweeter.client.Model.Observer.LoadingObserver;
import edu.byu.cs.tweeter.client.Model.Observer.StateObserver;
import edu.byu.cs.tweeter.client.Model.Service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

// IsFollowerHandler
public class IsFollowerHandler extends BackgroundTaskHandler<StateObserver<Boolean>>
{

    public IsFollowerHandler(StateObserver<Boolean> observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(StateObserver<Boolean> observer, Bundle data) {
        boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        observer.handleSuccess(isFollower);
    }
}
