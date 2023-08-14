package edu.byu.cs.tweeter.client.Presenter;

import edu.byu.cs.tweeter.client.Model.Observer.ServiceObserver;
import edu.byu.cs.tweeter.client.Model.Observer.StateObserver;
import edu.byu.cs.tweeter.client.Model.Service.UserService;
import edu.byu.cs.tweeter.client.View.PagedView;
import edu.byu.cs.tweeter.client.View.View;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter<PagedView<T>>
{
    private T lastItem;
    private boolean hasMorePages;
    private boolean isLoading = false;
    private final int pageSize = 25;
    public PagedPresenter(PagedView<T> view)
    {
        super(view);
    }
    public int getPageSize()
    {
        return pageSize;
    }
    public T getLastItem() {
        return lastItem;
    }
    public void setLastItem(T lastItem) {
        this.lastItem = lastItem;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }
    public boolean isLoading() {
        return isLoading;
    }
    public void setLoading(boolean loading) {
        isLoading = loading;
    }
    public void loadMoreItems(User user)
    {
        if (!isLoading) // This guard is important for avoiding a race condition in the scrolling code.
        {
            setLoading(true);
            view.setLoadingFooter(true);
        }
        doLoading(user);
    }
    public void displayUser(String userAlias)
    {
        userService.displayUser(userAlias, new getUserObserver());
    }
    public abstract void doLoading(User user);
    private class getUserObserver implements StateObserver<User>
    {
        @Override
        public void handleSuccess(User item)
        {
            view.showUser(item);
        }
        @Override
        public void handleFailure(String message)
        {
            view.displayMessage("Failed to get user's profile: " + message);
        }
        @Override
        public void handleException(Exception ex)
        {
            view.displayMessage("Failed to get user's profile because of exception: " + ex.getMessage());
        }
    }
}
