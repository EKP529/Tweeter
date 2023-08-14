package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public abstract class PagedRequest<T> extends AuthenticatedRequest
{
    private String targetUserAlias;
    private int limit;
    private T lastItem;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    protected PagedRequest()
    {
        super();
    }

    /**
     * Creates an instance.
     *
     * @param targetUserAlias the alias of the user whose items are to be returned.
     * @param limit the maximum number of followees to return.
     * @param lastItem the text for the last item that was returned in the previous request (null if
     *                     there was no previous request or if no items were returned in the
     *                     previous request).
     */
    protected PagedRequest(AuthToken authToken, String targetUserAlias, int limit, T lastItem) {
        super(authToken);
        this.targetUserAlias = targetUserAlias;
        this.limit = limit;
        this.lastItem = lastItem;
    }

    /**
     * Returns the user whose items are to be returned by this request.
     *
     * @return the user.
     */
    public String getTargetUserAlias() {
        return targetUserAlias;
    }

    /**
     * Sets the user.
     *
     * @param targetUserAlias the user.
     */
    public void setTargetUserAlias(String targetUserAlias) { this.targetUserAlias = targetUserAlias; }

    /**
     * Returns the number representing the maximum number of followees to be returned by this request.
     *
     * @return the limit.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Sets the limit.
     *
     * @param limit the limit.
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Returns the last item that was returned in the previous request or null if there was no
     * previous request or if no items were returned in the previous request.
     *
     * @return the last followee.
     */
    public T getLastItem() {
        return lastItem;
    }

    /**
     * Sets the last followee.
     *
     * @param lastItem the last item.
     */
    public void setLastItem(T lastItem) {
        this.lastItem = lastItem;
    }
}
