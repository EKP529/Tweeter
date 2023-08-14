package edu.byu.cs.tweeter.server.Lambda.FollowServices;

import edu.byu.cs.tweeter.server.Lambda.DynamoDBHandler;
import edu.byu.cs.tweeter.server.Service.FollowService;

public abstract class FollowServiceHandler extends DynamoDBHandler
{
    protected FollowService getFollowService()
    {
        return new FollowService(getFactory());
    }
}
