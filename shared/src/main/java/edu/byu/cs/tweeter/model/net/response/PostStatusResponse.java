package edu.byu.cs.tweeter.model.net.response;

public class PostStatusResponse extends Response
{
    /**
     * Creates a response indicating that the corresponding request was successful.
     */
    public PostStatusResponse()
    {
        super(true);
    }

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success indicator to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public PostStatusResponse(String message)
    {
        super(false, message);
    }
}