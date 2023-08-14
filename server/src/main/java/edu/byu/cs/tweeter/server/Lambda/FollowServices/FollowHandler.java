package edu.byu.cs.tweeter.server.Lambda.FollowServices;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.server.Lambda.FollowServices.FollowServiceHandler;

/**
 * An AWS lambda function that follows a user and returns the status code for the follow.
 */
public class FollowHandler extends FollowServiceHandler implements RequestHandler<FollowRequest, FollowResponse>
{
    /**
     * Returns the status code of the post specified in the request.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the count.
     */
    @Override
    public FollowResponse handleRequest(FollowRequest request, Context context)
    {
        return getFollowService().follow(request);
    }
}
