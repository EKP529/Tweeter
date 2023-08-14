package edu.byu.cs.tweeter.server.Lambda.FollowServices;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.byu.cs.tweeter.model.net.request.GetCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetCountResponse;
import edu.byu.cs.tweeter.server.Lambda.FollowServices.FollowServiceHandler;

public class GetFollowingCountHandler extends FollowServiceHandler implements RequestHandler<GetCountRequest, GetCountResponse>
{
    /**
     * Returns the following count for the user specified in the request.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the count.
     */
    @Override
    public GetCountResponse handleRequest(GetCountRequest request, Context context)
    {
        System.out.println(request.getAuthToken());
        return getFollowService().getFollowingCount(request);
    }
}
