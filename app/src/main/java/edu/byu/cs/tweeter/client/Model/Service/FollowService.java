package edu.byu.cs.tweeter.client.Model.Service;

import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.FollowTask;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.GetFollowersTask;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.GetFollowingTask;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler.FollowHandler;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler.GetFollowersCountHandler;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler.GetFollowersHandler;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler.GetFollowingCountHandler;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler.GetFollowingHandler;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler.UnfollowHandler;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.IsFollowerTask;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.UnfollowTask;
import edu.byu.cs.tweeter.client.Model.Observer.LoadingObserver;
import edu.byu.cs.tweeter.client.Model.Observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.Model.Observer.StateObserver;
import edu.byu.cs.tweeter.client.Model.Cache.Cache;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends Service
{
    public static final String GET_FOLLOWING_URL_PATH = "/getfollowing";
    public static final String GET_FOLLOWERS_URL_PATH = "/getfollowers";
    public static final String FOLLOW_URL_PATH = "/follow";
    public static final String UNFOLLOW_URL_PATH = "/unfollow";
    public static final String IS_FOLLOWER_URL_PATH = "/isfollower";
    public static final String GET_FOLLOWING_COUNT_URL_PATH = "/getfollowingcount";
    public static final String GET_FOLLOWERS_COUNT_URL_PATH = "/getfollowerscount";
    public void follow(User selectedUser, SimpleNotificationObserver observer)
    {
        FollowTask followTask = new FollowTask(getAuthToken(), getCurrUser(),
                selectedUser, new FollowHandler(observer));
        executeTask(followTask);
    }
    public void loadMoreFollowees(User user, int pageSize, User lastFollowee, LoadingObserver<User> observer)
    {
        GetFollowingTask getFollowingTask = new GetFollowingTask(getAuthToken(),
                user, pageSize, lastFollowee, new GetFollowingHandler(observer));
        executeTask(getFollowingTask);
    }

    public void loadMoreFollowers(User user, int pageSize, User lastFollower, LoadingObserver<User> observer)
    {
        GetFollowersTask getFollowersTask = new GetFollowersTask(getAuthToken(),
                user, pageSize, lastFollower, new GetFollowersHandler(observer));
        executeTask(getFollowersTask);
    }

    public void isFollower(User selectedUser, StateObserver<Boolean> observer)
    {
        IsFollowerTask isFollowerTask = new IsFollowerTask(getAuthToken(),
                getCurrUser(), selectedUser, new IsFollowerHandler(observer));
        executeTask(isFollowerTask);
    }
    public void getFollowersCount(User selectedUser, StateObserver<Integer> observer)
    {
       GetFollowersCountTask followersCountTask = new GetFollowersCountTask(getAuthToken(),
                selectedUser, new GetFollowersCountHandler(observer));
        executeTask(followersCountTask);
    }
    public void getFollowingCount(User selectedUser, StateObserver<Integer> observer)
    {
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(getAuthToken(),
                selectedUser, new GetFollowingCountHandler(observer));
        executeTask(followingCountTask);
    }
    public void unfollow(User selectedUser, SimpleNotificationObserver observer)
    {
        UnfollowTask unfollowTask = new UnfollowTask(getAuthToken(), getCurrUser(),
                selectedUser, new UnfollowHandler(observer));
        executeTask(unfollowTask);
    }

}
