package edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.Model.Observer.StateObserver;
import edu.byu.cs.tweeter.client.Model.Service.FollowService;

// GetFollowingCountHandler
public class GetFollowingCountHandler extends BackgroundTaskHandler<StateObserver<Integer>>
{
    public GetFollowingCountHandler(StateObserver<Integer> observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(StateObserver<Integer> observer, Bundle data) {
        long count = data.getInt(GetFollowingCountTask.COUNT_KEY);
        observer.handleSuccess((int)count);
    }
}
