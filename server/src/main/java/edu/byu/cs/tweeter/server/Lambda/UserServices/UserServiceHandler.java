package edu.byu.cs.tweeter.server.Lambda.UserServices;

import edu.byu.cs.tweeter.server.Lambda.DynamoDBHandler;
import edu.byu.cs.tweeter.server.Service.UserService;

public abstract class UserServiceHandler extends DynamoDBHandler
{
    protected UserService getUserService()
    {
        return new UserService(getFactory());
    }
}
