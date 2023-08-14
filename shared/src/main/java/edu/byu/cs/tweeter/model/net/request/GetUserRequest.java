package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class GetUserRequest extends AuthenticatedRequest
{
    private String alias;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private GetUserRequest()
    {
        super();
    }

    /**
     * Creates an instance.
     *
     * @param alias the alias of the user.
     *
     */
    public GetUserRequest(AuthToken authToken, String alias)
    {
        super(authToken);
        this.alias = alias;
    }

    /**
     * Returns the user's alias.
     *
     * @return the user's alias.
     */
    public String getAlias()
    {
        return alias;
    }

    /**
     * Sets the user's alias.
     *
     * @param alias the user's alias.
     */
    public void setAlias(String alias)
    {
        this.alias = alias;
    }
}
