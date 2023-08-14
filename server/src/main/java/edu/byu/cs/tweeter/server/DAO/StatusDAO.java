package edu.byu.cs.tweeter.server.DAO;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.DataPage;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.Feeds;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.Stories;

public interface StatusDAO
{
    DataPage<Stories> getStory(String targetUserAlias, int limit, Long lastItem);
    DataPage<Feeds> getFeed(String targetUserAlias, int limit, Long lastItem);
    void putStatusToStory(Status status);
    void updateFeedsTable(List<String> followers, Status status);
}
