package edu.byu.cs.tweeter.client.Model.BackgroundTask.Task;

import android.os.Handler;

import edu.byu.cs.tweeter.client.Model.Service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetCountResponse;

/**
 * Background task that queries how many followers a user has.
 */
public class GetFollowersCountTask extends GetCountTask
{

    public GetFollowersCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, targetUser, messageHandler);
    }

    @Override
    protected GetCountResponse runCountTask() throws Exception
    {
        String targetUserAlias = getTargetUser() == null ? null : getTargetUser().getAlias();
        GetCountRequest request = new GetCountRequest(getAuthToken(), targetUserAlias);
        return getServerFacade().getCount(request, FollowService.GET_FOLLOWERS_COUNT_URL_PATH);
    }
}
