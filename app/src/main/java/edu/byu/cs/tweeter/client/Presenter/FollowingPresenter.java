package edu.byu.cs.tweeter.client.Presenter;

import edu.byu.cs.tweeter.client.Model.Observer.LoadingObserver;
import edu.byu.cs.tweeter.client.Model.Observer.StateObserver;
import edu.byu.cs.tweeter.client.Model.Service.FollowService;
import edu.byu.cs.tweeter.client.View.PagedView;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public class FollowingPresenter extends PagedPresenter<User>
{
    private final FollowService followService;
    public FollowingPresenter(PagedView<User> view)
    {
        super(view);
        this.followService = new FollowService();
    }
    @Override
    public void doLoading(User user)
    {
        followService.loadMoreFollowees(user, getPageSize(), getLastItem(), new getFollowingObserver());
    }
    private class getFollowingObserver implements LoadingObserver<User>
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
            view.displayMessage("Failed to get following: " + message);
        }
        @Override
        public void handleException(Exception ex)
        {
            setLoading(false);
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get following because of exception: " + ex.getMessage());
        }
    }
}
