package edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.GetFeedTask;
import edu.byu.cs.tweeter.client.Model.Observer.LoadingObserver;
import edu.byu.cs.tweeter.model.domain.Status;

public abstract class PagedTaskHandler<T> extends BackgroundTaskHandler<LoadingObserver<T>>
{
    public PagedTaskHandler(LoadingObserver<T> observer)
    {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(LoadingObserver<T> observer, Bundle data)
    {
        List<T> items = (List<T>) data.getSerializable(GetFeedTask.ITEMS_KEY);
        boolean hasMorePages = data.getBoolean(GetFeedTask.MORE_PAGES_KEY);
        observer.handleSuccess(items, hasMorePages);
    }
}
