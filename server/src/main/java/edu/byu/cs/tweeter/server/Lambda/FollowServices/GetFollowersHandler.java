package edu.byu.cs.tweeter.server.Lambda.FollowServices;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.PagedUserRequest;
import edu.byu.cs.tweeter.model.net.response.PagedUserResponse;
import edu.byu.cs.tweeter.server.Lambda.FollowServices.FollowServiceHandler;

/**
 * An AWS lambda function that returns the users a user is being followed by.
 */
public class GetFollowersHandler extends FollowServiceHandler implements RequestHandler<PagedUserRequest, PagedUserResponse>
{

    /**
     * Returns the users that the user specified in the request. Uses information in
     * the request object to limit the number of followers returned and to return the next set of
     * followers after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the followers.
     */
    @Override
    public PagedUserResponse handleRequest(PagedUserRequest request, Context context)
    {
        return getFollowService().getFollowers(request);
    }
}