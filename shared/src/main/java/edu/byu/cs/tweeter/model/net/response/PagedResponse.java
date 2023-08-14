package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

/**
 * A response that can indicate whether there is more data available from the server.
 */
public abstract class PagedResponse<T> extends Response
{
    private boolean hasMorePages;
    private List<T> items;
    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param items the items to be included in the result.
     * @param hasMorePages an indicator of whether more data is available for the request.
     */
    protected PagedResponse(List<T> items, boolean hasMorePages)
    {
        super(true);
        this.items = items;
        this.hasMorePages = hasMorePages;
    }

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success and more pages indicators to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    protected PagedResponse(String message)
    {
        super(false, message);
        this.hasMorePages = false;
    }

    /**
     * An indicator of whether more data is available from the server. A value of true indicates
     * that the result was limited by a maximum value in the request and an additional request
     * would return additional data.
     *
     * @return true if more data is available; otherwise, false.
     */
    public boolean getHasMorePages() {
        return hasMorePages;
    }
    /**
     * Returns the items for the corresponding request.
     *
     * @return the items.
     */
    public List<T> getItems() {
        return items;
    }

    public void setHasMorePages(boolean b) {
        hasMorePages = b;
    }
}
