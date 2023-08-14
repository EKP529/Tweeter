package edu.byu.cs.tweeter.server.Lambda.UserServices;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;

/**
 * An AWS lambda function that logs a user in and returns the user object and an auth code for
 * a successful login.
 */
public class LoginHandler extends UserServiceHandler implements RequestHandler<LoginRequest, AuthenticateResponse>
{
    @Override
    public AuthenticateResponse handleRequest(LoginRequest loginRequest, Context context)
    {
        return getUserService().login(loginRequest);
    }
}
