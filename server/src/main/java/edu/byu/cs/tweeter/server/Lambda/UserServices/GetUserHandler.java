package edu.byu.cs.tweeter.server.Lambda.UserServices;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;

public class GetUserHandler extends UserServiceHandler implements RequestHandler<GetUserRequest, GetUserResponse>
{
    /**
     * Returns the user specified in the request.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the count.
     */
    @Override
    public GetUserResponse handleRequest(GetUserRequest request, Context context)
    {
        return getUserService().getUser(request);
    }
}
