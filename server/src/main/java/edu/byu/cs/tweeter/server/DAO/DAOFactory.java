package edu.byu.cs.tweeter.server.DAO;

public interface DAOFactory
{
    FollowDAO getFollowDAO();
    StatusDAO getStatusDAO();
    UserDAO getUserDAO();
    AuthTokenDAO getAuthTokenDAO();
}
