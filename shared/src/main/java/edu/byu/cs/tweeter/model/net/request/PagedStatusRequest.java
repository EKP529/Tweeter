package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class PagedStatusRequest extends PagedRequest<Long>
{
    private PagedStatusRequest()
    {
        super();
    }

    public PagedStatusRequest(AuthToken authToken, String targetUserAlias, int limit, Long lastItem)
    {
        super(authToken, targetUserAlias, limit, lastItem);
    }
}
