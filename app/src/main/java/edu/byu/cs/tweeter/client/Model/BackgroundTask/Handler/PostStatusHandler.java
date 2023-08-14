package edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.PostStatusTask;
import edu.byu.cs.tweeter.client.Model.Observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.Model.Service.StatusService;

// PostStatusHandler
public class PostStatusHandler extends BackgroundTaskHandler<SimpleNotificationObserver>
{
    public PostStatusHandler(SimpleNotificationObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(SimpleNotificationObserver observer, Bundle data) {
        observer.handleSuccess();
    }
}
