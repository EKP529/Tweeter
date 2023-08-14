package edu.byu.cs.tweeter.client.Model.Service;

import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.GetFeedTask;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.GetStoryTask;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler.GetFeedHandler;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler.GetStoryHandler;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler.PostStatusHandler;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.PostStatusTask;
import edu.byu.cs.tweeter.client.Model.Observer.LoadingObserver;
import edu.byu.cs.tweeter.client.Model.Observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService extends Service
{
    public static final String GET_STORY_URL_PATH = "/getstory";
    public static final String GET_FEED_URL_PATH = "/getfeed";
    public static final String POST_STATUS_URL_PATH = "/poststatus";
    public void loadMoreStory(User user, int pageSize, Status lastStatus, LoadingObserver<Status> observer)
    {
        GetStoryTask getStoryTask = new GetStoryTask(getAuthToken(),
                user, pageSize, lastStatus, new GetStoryHandler(observer));
        executeTask(getStoryTask);
    }
    public void loadMoreFeed(User user, int pageSize, Status lastStatus, LoadingObserver<Status> observer)
    {
        GetFeedTask getFeedTask = new GetFeedTask(getAuthToken(),
                user, pageSize, lastStatus, new GetFeedHandler(observer));
        executeTask(getFeedTask);
    }
    public void postStatus(Status newStatus, SimpleNotificationObserver observer)
    {
        PostStatusTask statusTask = new PostStatusTask(getAuthToken(),
                newStatus, new PostStatusHandler(observer));
        executeTask(statusTask);
    }

}
