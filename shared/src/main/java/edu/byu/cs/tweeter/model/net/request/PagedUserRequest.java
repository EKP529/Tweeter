package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class PagedUserRequest extends PagedRequest<String>
{
    private PagedUserRequest()
    {
        super();
    }

    public PagedUserRequest(AuthToken authToken, String targetUserAlias, int limit, String lastItem)
    {
        super(authToken, targetUserAlias, limit, lastItem);
    }
}
