package edu.byu.cs.tweeter.server.Lambda.FollowServices;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.server.Lambda.FollowServices.FollowServiceHandler;

public class IsFollowerHandler extends FollowServiceHandler implements RequestHandler<IsFollowerRequest, IsFollowerResponse>
{
    /**
     * Returns the true/false state of following between follower and followee specified in request.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the count.
     */
    @Override
    public IsFollowerResponse handleRequest(IsFollowerRequest request, Context context)
    {
        return getFollowService().isFollower(request);
    }
}
