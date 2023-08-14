package edu.byu.cs.tweeter.model.net.request;


import edu.byu.cs.tweeter.model.domain.AuthToken;

public class GetCountRequest extends AuthenticatedRequest
{
    private String targetUserAlias;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private GetCountRequest()
    {
        super();
    }

    /**
     * Creates an instance.
     *
     * @param targetUserAlias the alias of the user whose items count is to be returned.
     *
     */
    public GetCountRequest(AuthToken authToken, String targetUserAlias)
    {
        super(authToken);
        this.targetUserAlias = targetUserAlias;
    }

    /**
     * Returns the user's alias whose items' count is to be returned by this request.
     *
     * @return the user.
     */
    public String getTargetUserAlias()
    {
        return targetUserAlias;
    }

    /**
     * Sets the user.
     *
     * @param targetUserAlias the user.
     */
    public void setTargetUserAlias(String targetUserAlias)
    {
        this.targetUserAlias = targetUserAlias;
    }
}
