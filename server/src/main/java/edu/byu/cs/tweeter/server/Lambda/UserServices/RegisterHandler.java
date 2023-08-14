package edu.byu.cs.tweeter.server.Lambda.UserServices;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;

/**
 * An AWS lambda function that logs a user in and returns the user object and an auth code for
 * a successful login.
 */
public class RegisterHandler extends UserServiceHandler implements RequestHandler<RegisterRequest, AuthenticateResponse>
{
    @Override
    public AuthenticateResponse handleRequest(RegisterRequest registerRequest, Context context)
    {
        return getUserService().register(registerRequest);
    }
}