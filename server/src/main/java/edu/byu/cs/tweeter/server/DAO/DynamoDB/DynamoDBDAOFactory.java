package edu.byu.cs.tweeter.server.DAO.DynamoDB;

import edu.byu.cs.tweeter.server.DAO.AuthTokenDAO;
import edu.byu.cs.tweeter.server.DAO.DAOFactory;
import edu.byu.cs.tweeter.server.DAO.FollowDAO;
import edu.byu.cs.tweeter.server.DAO.StatusDAO;
import edu.byu.cs.tweeter.server.DAO.UserDAO;

public class DynamoDBDAOFactory implements DAOFactory
{
    @Override
    public FollowDAO getFollowDAO() {
        return new DynamoDBFollowDAO();
    }
    @Override
    public StatusDAO getStatusDAO() {
        return new DynamoDBStatusDAO();
    }
    @Override
    public UserDAO getUserDAO() {
        return new DynamoDBUserDAO();
    }
    @Override
    public AuthTokenDAO getAuthTokenDAO() { return new DynamoDBAuthTokenDAO(); }
}
