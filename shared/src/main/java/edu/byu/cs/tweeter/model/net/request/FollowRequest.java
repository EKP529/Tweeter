package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowRequest extends AuthenticatedRequest
{
    private User follower;
    private User followee;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private FollowRequest()
    {
        super();
    }

    /**
     * Creates an instance.
     *
     * @param followee the user being followed.
     *
     */
    public FollowRequest(AuthToken authToken, User follower, User followee)
    {
        super(authToken);
        this.follower = follower;
        this.followee = followee;
    }

    /**
     * Returns the user being followed.
     *
     * @return the user being followed.
     */
    public User getFollowee()
    {
        return followee;
    }

    /**
     * Sets the user being followed.
     *
     * @param followee the user being followed.
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
