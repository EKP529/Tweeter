package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class IsFollowerRequest extends AuthenticatedRequest
{
    private String followerAlias;
    private String followeeAlias;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private IsFollowerRequest()
    {
        super();
    }

    /**
     * Creates an instance.
     *
     * @param followerAlias the alias of the alleged follower.
     * @param followeeAlias the alias of the alleged followee.
     *
     */
    public IsFollowerRequest(AuthToken authToken, String followerAlias, String followeeAlias)
    {
        super(authToken);
        this.followerAlias = followerAlias;
        this.followeeAlias = followeeAlias;
    }

    /**
     * Returns the alleged follower alias.
     *
     * @return the user.
     */
    public String getFollowerAlias()
    {
        return followerAlias;
    }

    /**
     * Sets the alleged follower alias.
     *
     * @param followerAlias the alleged follower.
     */
    public void setFollowerAlias(String followerAlias)
    {
        this.followerAlias = followerAlias;
    }

    /**
     * Returns the alleged followee alias.
     *
     * @return the followee alias.
     */
    public String getFolloweeAlias()
    {
        return followeeAlias;
    }

    /**
     * Sets the alleged followee alias.
     *
     * @param followeeAlias the alleged followee alias.
     */
    public void setFolloweeAlias(String followeeAlias)
    {
        this.followeeAlias = followeeAlias;
    }
}
