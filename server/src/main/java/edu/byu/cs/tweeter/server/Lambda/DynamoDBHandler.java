package edu.byu.cs.tweeter.server.Lambda;

import edu.byu.cs.tweeter.server.DAO.DynamoDB.DynamoDBDAOFactory;

public abstract class DynamoDBHandler
{
    private DynamoDBDAOFactory factory = new DynamoDBDAOFactory();
    protected DynamoDBDAOFactory getFactory() { return factory; }
}
