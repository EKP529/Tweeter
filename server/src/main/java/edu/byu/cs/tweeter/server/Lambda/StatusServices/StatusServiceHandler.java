package edu.byu.cs.tweeter.server.Lambda.StatusServices;

import edu.byu.cs.tweeter.server.Lambda.DynamoDBHandler;
import edu.byu.cs.tweeter.server.Service.StatusService;

public abstract class StatusServiceHandler extends DynamoDBHandler
{
    protected StatusService getStatusService()
    {
        return new StatusService(getFactory());
    }
}
