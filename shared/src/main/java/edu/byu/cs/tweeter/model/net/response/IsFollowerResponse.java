package edu.byu.cs.tweeter.model.net.response;

public class IsFollowerResponse extends Response
{
    private boolean isFollower;

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param isFollower the true/false to be included in the result.
     */
    public IsFollowerResponse(boolean isFollower) {
        super(true);
        this.isFollower = isFollower;
    }

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success indicator to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public IsFollowerResponse(String message) {
        super(false, message);
    }

    /**
     * Returns true/false for the corresponding request.
     *
     * @return true/false for follower state.
     */
    public boolean isFollower() {
        return isFollower;
    }
}
