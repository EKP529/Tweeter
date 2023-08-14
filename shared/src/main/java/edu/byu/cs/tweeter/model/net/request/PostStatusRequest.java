package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class PostStatusRequest extends AuthenticatedRequest
{
    private Status status;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private PostStatusRequest()
    {
        super();
    }

    /**
     * Creates an instance.
     *
     * @param status the alias of the user.
     *
     */
    public PostStatusRequest(AuthToken authToken, Status status)
    {
        super(authToken);
        this.status = status;
    }

    /**
     * Returns the user's status.
     *
     * @return the user's status.
     */
    public Status getStatus()
    {
        return status;
    }

    /**
     * Sets the user's status.
     *
     * @param status the user's status.
     */
    public void setStatus(Status status)
    {
        this.status = status;
    }
}
