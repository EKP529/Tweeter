package edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler;

import android.os.Bundle;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.Model.Observer.StateObserver;

// GetFollowersCountHandler
public class GetFollowersCountHandler extends BackgroundTaskHandler<StateObserver<Integer>>
{
    public GetFollowersCountHandler(StateObserver<Integer> observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(StateObserver<Integer> observer, Bundle data)
    {
        int count = data.getInt(GetFollowersCountTask.COUNT_KEY);
        observer.handleSuccess(count);
    }
}
