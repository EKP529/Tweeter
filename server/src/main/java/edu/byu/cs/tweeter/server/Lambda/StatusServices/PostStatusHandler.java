package edu.byu.cs.tweeter.server.Lambda.StatusServices;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

/**
 * An AWS lambda function that logs a user in and returns the user object and an auth code for
 * a successful login.
 */
public class PostStatusHandler extends StatusServiceHandler implements RequestHandler<PostStatusRequest, PostStatusResponse>
{
    /**
     * Returns the status code of the post specified in the request.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the count.
     */
    @Override
    public PostStatusResponse handleRequest(PostStatusRequest request, Context context)
    {
        return getStatusService().postStatus(request);
    }
}