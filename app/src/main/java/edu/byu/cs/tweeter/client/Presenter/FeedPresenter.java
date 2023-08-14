package edu.byu.cs.tweeter.client.Presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.Model.Observer.LoadingObserver;
import edu.byu.cs.tweeter.client.Model.Observer.StateObserver;
import edu.byu.cs.tweeter.client.Model.Service.StatusService;
import edu.byu.cs.tweeter.client.View.PagedView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status>
{
    private final StatusService statusService;
    public FeedPresenter(PagedView<Status> view)
    {
        super(view);
        this.statusService = new StatusService();
    }
    @Override
    public void doLoading(User user)
    {
        statusService.loadMoreFeed(user, getPageSize(), getLastItem(), new getFeedObserver());
    }
    private class getFeedObserver implements LoadingObserver<Status>
    {
        @Override
        public void handleSuccess(List<Status> items, boolean hasMorePages)
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
            view.displayMessage("Failed to get feed: " + message);
        }

        @Override
        public void handleException(Exception ex)
        {
            setLoading(false);
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get feed because of exception: " + ex.getMessage());
        }
    }
}
