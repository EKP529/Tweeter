package edu.byu.cs.tweeter.server.DAO;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.Users;

public interface UserDAO
{
    Users getUser(String alias);
    Users putUser(User user, String passwordHash);
    void updateFollowersCount(String alias, char type);
    void updateFollowingCount(String alias, char type);
    void clearTable(List<Users> usersRows);
    void putUserBatch(List<Users> usersRows);
}
