package edu.byu.cs.tweeter.model.net.response;

public class LogoutResponse extends Response
{
    /**
     * Creates a response indicating that the corresponding request was successful.
     */
    public LogoutResponse()
    {
        super(true);
    }

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success indicator to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public LogoutResponse(String message)
    {
        super(false, message);
    }
}
