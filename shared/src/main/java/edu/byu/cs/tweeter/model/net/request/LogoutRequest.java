package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class LogoutRequest extends AuthenticatedRequest
{
    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private LogoutRequest()
    {
        super();
    }

    /**
     * Creates an instance.
     */
    public LogoutRequest(AuthToken authToken)
    {
        super(authToken);
    }
}
