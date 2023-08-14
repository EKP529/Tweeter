package edu.byu.cs.tweeter.client.Model.BackgroundTask.Task;

import android.os.Handler;
import edu.byu.cs.tweeter.client.Model.Service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedUserRequest;
import edu.byu.cs.tweeter.model.net.response.PagedUserResponse;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedUserTask
{

    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollowee, messageHandler);
    }
    @Override
    protected PagedUserResponse getItems() throws Exception
    {
        String targetUserAlias = getTargetUser() == null ? null : getTargetUser().getAlias();
        String lastFolloweeAlias = getLastItem() == null ? null : getLastItem().getAlias();
        PagedUserRequest request = new PagedUserRequest(getAuthToken(), targetUserAlias,
                getLimit(), lastFolloweeAlias);

        return getServerFacade().getUsers(request, FollowService.GET_FOLLOWING_URL_PATH);
    }
}
