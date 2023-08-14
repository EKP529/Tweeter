package edu.byu.cs.tweeter.server.Lambda.UserServices;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;

/**
 * An AWS lambda function that logs a user out and returns the status code for the operation.
 */
public class LogoutHandler extends UserServiceHandler implements RequestHandler<LogoutRequest, LogoutResponse>
{
    /**
     * Returns the status code of the post specified in the request.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the count.
     */
    @Override
    public LogoutResponse handleRequest(LogoutRequest request, Context context)
    {
        return getUserService().logout(request);
    }
}
