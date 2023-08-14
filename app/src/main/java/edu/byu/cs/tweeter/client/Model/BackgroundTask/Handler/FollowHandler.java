package edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.FollowTask;
import edu.byu.cs.tweeter.client.Model.Observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.Model.Service.FollowService;

// FollowHandler
public class FollowHandler extends BackgroundTaskHandler<SimpleNotificationObserver>
{
    public FollowHandler(SimpleNotificationObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(SimpleNotificationObserver observer, Bundle data) {
        observer.handleSuccess();
    }
}
