package edu.byu.cs.tweeter.client.Presenter;

import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import edu.byu.cs.tweeter.client.Model.Observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.Model.Observer.StateObserver;
import edu.byu.cs.tweeter.client.Model.Service.FollowService;
import edu.byu.cs.tweeter.client.Model.Service.StatusService;
import edu.byu.cs.tweeter.client.View.View;
import edu.byu.cs.tweeter.client.Model.Cache.Cache;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;

public class MainPresenter extends Presenter<MainPresenter.MainView>
{
    private static final String LOG_TAG = "MainActivity";
    private StatusService statusService;
    private SimpleNotificationObserver observer;
    private final FollowService followservice;
    public interface MainView extends View
    {
        void logoutUser();
        void unfollow();
        void follow();
        void isFollower(boolean isFollower);
        void showFollowingCount(int count);
        void showFollowerCount(int count);
    }
    public MainPresenter(MainView view)
    {
        super(view);
        this.followservice = new FollowService();
    }
    protected StatusService getStatusService()
    {
        if (statusService == null)
        {
            statusService = new StatusService();
        }
        return statusService;
    }
    public SimpleNotificationObserver getPostStatusObserver()
    {
        if (observer == null)
        {
            observer = new PostStatusObserver();
        }
        return observer;
    }
    public void getFollowingCount(User selectedUser)
    {
        followservice.getFollowingCount(selectedUser, new getFollowingCountObserver());
    }
    public void postStatus(String post)
    {
        try
        {
            view.displayMessage("Posting Status...");
            Status newStatus = new Status(post, Cache.getInstance().getCurrUser(), System.currentTimeMillis(), parseURLs(post), parseMentions(post));
            getStatusService().postStatus(newStatus, getPostStatusObserver());
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            view.displayMessage("Failed to post the status because of exception: " + ex.getMessage());
        }
    }
    public void unfollow(User selectedUser)
    {
        followservice.unfollow(selectedUser, new UnfollowObserver());
    }
    public void follow(User selectedUser)
    {
        followservice.follow(selectedUser, new FollowObserver());
    }
    public void logoutUser()
    {
        userService.logoutUser(new LogoutObserver());
    }
    public void isFollower(User selectedUser)
    {
        followservice.isFollower(selectedUser, new IsFollowerObserver());
    }

    public void getFollowersCount(User selectedUser)
    {
        followservice.getFollowersCount(selectedUser, new getFollowersCountObserver());
    }

    public String getFormattedDateTime() throws ParseException
    {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    public List<String> parseURLs(String post)
    {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }
        return containedUrls;
    }

    public List<String> parseMentions(String post)
    {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z\\d]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }
        return containedMentions;
    }

    public int findUrlEndIndex(String word)
    {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    private class UnfollowObserver implements SimpleNotificationObserver
    {
        @Override
        public void handleSuccess()
        {
            view.unfollow();
        }
        @Override
        public void handleFailure(String message)
        {
            view.displayMessage("Failed to unfollow: " + message);
        }
        @Override
        public void handleException(Exception ex)
        {
            view.displayMessage("Failed to unfollow because of exception: " + ex.getMessage());
        }
    }
    private class FollowObserver implements SimpleNotificationObserver
    {
        @Override
        public void handleSuccess()
        {
            view.follow();
        }
        @Override
        public void handleFailure(String message)
        {
            view.displayMessage("Failed to follow: " + message);
        }
        @Override
        public void handleException(Exception ex)
        {
            view.displayMessage("Failed to follow because of exception: " + ex.getMessage());
        }
    }
    protected class PostStatusObserver implements SimpleNotificationObserver
    {
        @Override
        public void handleSuccess()
        {
            view.displayMessage("Successfully Posted!");
        }
        @Override
        public void handleFailure(String message)
        {
            view.displayMessage("Failed to post the status: " + message);
        }
        @Override
        public void handleException(Exception ex)
        {
            view.displayMessage("Failed to post the status because of exception: " + ex.getMessage());
        }
    }
    private class LogoutObserver implements SimpleNotificationObserver
    {
        @Override
        public void handleSuccess()
        {
            view.logoutUser();
        }
        @Override
        public void handleFailure(String message)
        {
            view.displayMessage("Failed to logout: " + message);
        }
        @Override
        public void handleException(Exception ex)
        {
            view.displayMessage("Failed to logout because of exception: " + ex.getMessage());
        }
    }
    private class getFollowingCountObserver implements StateObserver<Integer>
    {
        @Override
        public void handleSuccess(Integer item)
        {
            view.showFollowingCount(item);
        }
        @Override
        public void handleFailure(String message)
        {
            view.displayMessage("Failed to get following count: " + message);
        }
        @Override
        public void handleException(Exception ex)
        {
            view.displayMessage("Failed to get following count because of exception: " + ex.getMessage());
        }
    }
    private class getFollowersCountObserver implements StateObserver<Integer>
    {
        @Override
        public void handleSuccess(Integer item)
        {
            view.showFollowerCount(item);
        }
        @Override
        public void handleFailure(String message)
        {
            view.displayMessage("Failed to get followers count: " + message);
        }
        @Override
        public void handleException(Exception ex)
        {
            view.displayMessage("Failed to get followers count because of exception: " + ex.getMessage());
        }
    }
    private class IsFollowerObserver implements StateObserver<Boolean>
    {
        @Override
        public void handleSuccess(Boolean item)
        {
            view.isFollower(item);
        }
        @Override
        public void handleFailure(String message)
        {
            view.displayMessage("Failed to determine following relationship: " + message);
        }
        @Override
        public void handleException(Exception ex)
        {
            view.displayMessage("Failed to determine following relationship because of exception: " + ex.getMessage());
        }
    }

}
