package edu.byu.cs.tweeter.server.Lambda.FollowServices;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.byu.cs.tweeter.model.net.request.GetCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetCountResponse;
import edu.byu.cs.tweeter.server.Lambda.FollowServices.FollowServiceHandler;

public class GetFollowersCountHandler extends FollowServiceHandler implements RequestHandler<GetCountRequest, GetCountResponse>
{
    /**
     * Returns the followers count for the user specified in the request.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the count.
     */
    @Override
    public GetCountResponse handleRequest(GetCountRequest request, Context context)
    {
        return getFollowService().getFollowersCount(request);
    }
}
