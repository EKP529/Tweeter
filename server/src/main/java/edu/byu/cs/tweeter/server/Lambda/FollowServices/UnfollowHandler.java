package edu.byu.cs.tweeter.server.Lambda.FollowServices;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.Lambda.FollowServices.FollowServiceHandler;

/**
 * An AWS lambda function that unfollows a user and returns the status code for the unfollow.
 */
public class UnfollowHandler extends FollowServiceHandler implements RequestHandler<UnfollowRequest, UnfollowResponse>
{
    /**
     * Returns the status code of the post specified in the request.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the count.
     */
    @Override
    public UnfollowResponse handleRequest(UnfollowRequest request, Context context)
    {
        return getFollowService().unfollow(request);
    }
}
