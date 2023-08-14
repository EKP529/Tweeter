package edu.byu.cs.tweeter.model.net.response;

/**
 * A response that can indicate whether there is more count data available from the server.
 */
public class GetCountResponse extends Response
{
    private int count;

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param count the count to be included in the result.
     */
    public GetCountResponse(int count) {
        super(true);
        this.count = count;
    }

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success indicator to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public GetCountResponse(String message) {
        super(false, message);
    }

    /**
     * Returns the items' count for the corresponding request.
     *
     * @return the items.
     */
    public int getCount() {
        return count;
    }
}
