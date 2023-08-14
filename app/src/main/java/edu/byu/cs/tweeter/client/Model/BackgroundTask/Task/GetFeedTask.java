package edu.byu.cs.tweeter.client.Model.BackgroundTask.Task;

import android.os.Handler;
import edu.byu.cs.tweeter.client.Model.Service.StatusService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PagedStatusResponse;

/**
 * Background task that retrieves a page of statuses from a user's feed.
 */
public class GetFeedTask extends PagedStatusTask
{
    public GetFeedTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                       Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

    @Override
    protected PagedStatusResponse getItems() throws Exception
    {
        String targetUserAlias = getTargetUser() == null ? null : getTargetUser().getAlias();
        long num = 0;
        if (getLastItem() != null)
            num = getLastItem().getTimestamp();
        PagedStatusRequest request = new PagedStatusRequest(getAuthToken(), targetUserAlias,
                getLimit(), num);
        return getServerFacade().getStatuses(request, StatusService.GET_FEED_URL_PATH);
    }
}
