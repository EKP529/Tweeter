package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UnfollowRequest extends AuthenticatedRequest
{
    private User follower;
    private User followee;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private UnfollowRequest()
    {
        super();
    }

    /**
     * Creates an instance.
     *
     * @param followee the user being unfollowed.
     *
     */
    public UnfollowRequest(AuthToken authToken, User follower, User followee)
    {
        super(authToken);
        this.follower = follower;
        this.followee = followee;
    }

    /**
     * Returns the user being unfollowed.
     *
     * @return the user being unfollowed.
     */
    public User getFollowee()
    {
        return followee;
    }

    /**
     * Sets the user being unfollowed.
     *
     * @param followee the user being unfollowed.
     */
    public void setFollowee(User followee)
    {
        this.followee = followee;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }
}
