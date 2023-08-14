package edu.byu.cs.tweeter.client.Presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.Model.Observer.LoadingObserver;
import edu.byu.cs.tweeter.client.Model.Observer.StateObserver;
import edu.byu.cs.tweeter.client.Model.Service.FollowService;
import edu.byu.cs.tweeter.client.View.PagedView;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User>
{
    private final FollowService followService;
    public FollowersPresenter(PagedView<User> view)
    {
        super(view);
        this.followService = new FollowService();
    }
    @Override
    public void doLoading(User user)
    {
        followService.loadMoreFollowers(user, getPageSize(), getLastItem(), new getFollowersObserver());
    }
    private class getFollowersObserver implements LoadingObserver<User>
    {
        @Override
        public void handleSuccess(List<User> items, boolean hasMorePages)
        {
            setLoading(false);
            view.setLoadingFooter(false);
            setHasMorePages(hasMorePages);
            setLastItem((items.size() > 0) ? items.get(items.size() - 1) : null);
            view.addMoreItems(items);
        }
        @Override
        public void handleFailure(String message)
        {
            setLoading(false);
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get followers: " + message);
        }
        @Override
        public void handleException(Exception ex)
        {
            setLoading(false);
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get followers because of exception: " + ex.getMessage());
        }
    }
}
