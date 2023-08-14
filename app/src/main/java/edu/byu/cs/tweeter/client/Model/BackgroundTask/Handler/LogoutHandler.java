package edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.LogoutTask;
import edu.byu.cs.tweeter.client.Model.Observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.Model.Service.UserService;

public class LogoutHandler extends BackgroundTaskHandler<SimpleNotificationObserver>
{
    public LogoutHandler(SimpleNotificationObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(SimpleNotificationObserver observer, Bundle data) {
        observer.handleSuccess();
    }
}
