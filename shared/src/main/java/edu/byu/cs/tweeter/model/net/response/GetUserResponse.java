package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.User;

public class GetUserResponse extends Response
{
    private User user;

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param user the user to be included in the result.
     */
    public GetUserResponse(User user)
    {
        super(true);
        this.user = user;
    }

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success indicator to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public GetUserResponse(String message) {
        super(false, message);
    }

    /**
     * Returns the user for the corresponding request.
     *
     * @return the user.
     */
    public User getUser() {
        return user;
    }
}
