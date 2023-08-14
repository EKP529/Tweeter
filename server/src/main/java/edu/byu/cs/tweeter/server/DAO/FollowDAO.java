package edu.byu.cs.tweeter.server.DAO;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.DataPage;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.Follows;

public interface FollowDAO
{
    void putFollow(User follower, User followee);
    void deleteFollow(User follower, User followee);
    void putFollowBatch(List<Follows> followsRows);
    void clearTable(List<Follows> followsRows);
    Follows getFollow(String followerAlias, String followeeAlias);
    DataPage<Follows> getSecondaryFollowPage(String targetUserAlias, int limit, String lastItem);
    DataPage<Follows> getFollowPage(String targetUserAlias, int limit, String lastItem);
}
