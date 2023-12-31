package edu.byu.cs.tweeter.client.Model.BackgroundTask.Task;

import android.os.Bundle;
import android.os.Handler;
import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.client.Model.Net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;

public abstract class PagedTask<T> extends AuthenticatedTask
{

    public static final String ITEMS_KEY = "items";
    public static final String MORE_PAGES_KEY = "more-pages";

    /**
     * The user whose items are being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    private final User targetUser;

    /**
     * Maximum number of statuses to return (i.e., page size).
     */

    private final int limit;

    /**
     * The last status returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
    private final T lastItem;

    /**
     * The items returned in the current page of results.
     */
    private List<T> items;

    /**
     * Indicates whether there are more pages of items that can be retrieved on subsequent calls.
     */
    private boolean hasMorePages;
    private ServerFacade serverFacade;

    protected PagedTask(AuthToken authToken, User targetUser, int limit, T lastItem, Handler messageHandler) {
        super(authToken, messageHandler);
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastItem;
    }

    protected User getTargetUser() {
        return targetUser;
    }

    protected int getLimit() {
        return limit;
    }

    protected T getLastItem() {
        return lastItem;
    }

    @Override
    protected final void runTask() throws Exception
    {
        PagedResponse<T> response = getItems();
        if (response.isSuccess())
        {
            items = response.getItems();
            hasMorePages = response.getHasMorePages();
            // Call sendSuccessMessage if successful
            sendSuccessMessage();
        } else {
            // or call sendFailedMessage if not successful
            sendFailedMessage(response.getMessage());
        }

    }

    protected abstract PagedResponse<T> getItems() throws Exception;

    protected abstract List<User> getUsersForItems(List<T> items);

    @Override
    protected final void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(ITEMS_KEY, (Serializable) items);
        msgBundle.putBoolean(MORE_PAGES_KEY, hasMorePages);
    }

    /**
     * Returns an instance of {@link ServerFacade}. Allows mocking of the ServerFacade class for
     * testing purposes. All usages of ServerFacade should get their instance from this method to
     * allow for proper mocking.
     *
     * @return the instance.
     */
    public ServerFacade getServerFacade()
    {
        if(serverFacade == null)
        {
            serverFacade = new ServerFacade();
        }
        return new ServerFacade();
    }
}
