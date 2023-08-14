package edu.byu.cs.tweeter.client.Model.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.BackgroundTask;
import edu.byu.cs.tweeter.client.Model.Cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class Service
{
    protected AuthToken getAuthToken() { return Cache.getInstance().getCurrUserAuthToken(); }
    protected User getCurrUser() { return Cache.getInstance().getCurrUser();}
    protected void executeTask(BackgroundTask task)
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }
}
