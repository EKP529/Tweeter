package edu.byu.cs.tweeter.server.Service;

import java.util.ArrayList;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.GetCountRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.PagedUserRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.GetCountResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.PagedUserResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.DAO.DAOFactory;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.DataPage;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.Follows;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.Users;
import edu.byu.cs.tweeter.server.DAO.FollowDAO;
import edu.byu.cs.tweeter.server.DAO.UserDAO;

/**
 * Contains the business logic for getting the users a user is following or being followed by.
 */
public class FollowService extends Service
{
    public FollowService(DAOFactory factory)
    {
        super(factory);
    }
    public interface GetUsersStrategy {
        DataPage<Follows> getPage();
        User getUser(Follows followsRow);
    }
    public PagedUserResponse getFollowers(PagedUserRequest request)
    {
        GetUsersStrategy strategy = new GetUsersStrategy() {
            @Override
            public DataPage<Follows> getPage() {
                return getFollowDAO().getSecondaryFollowPage(
                        request.getTargetUserAlias(),
                        request.getLimit(),
                        request.getLastItem());
            }

            @Override
            public User getUser(Follows followsRow) {
                String[] name = followsRow.getFollower_name().split(" ");
                String alias = followsRow.getFollower_handle();
                String imageURL = followsRow.getFollower_imageURL();
                return new User(name[0], name[1], alias, imageURL);
            }
        };
        return getUsers(request, strategy);

    }
    public PagedUserResponse getFollowees(PagedUserRequest request)
    {
        GetUsersStrategy strategy = new GetUsersStrategy() {
            @Override
            public DataPage<Follows> getPage() {
                return getFollowDAO().getFollowPage(
                        request.getTargetUserAlias(),
                        request.getLimit(),
                        request.getLastItem());
            }

            @Override
            public User getUser(Follows followsRow) {
                String[] name = followsRow.getFollowee_name().split(" ");
                String alias = followsRow.getFollowee_handle();
                String imageURL = followsRow.getFollowee_imageURL();
                return new User(name[0], name[1], alias, imageURL);
            }
        };
        return getUsers(request, strategy);
    }
    private PagedUserResponse getUsers(PagedUserRequest request, GetUsersStrategy strategy)
    {
        if(request.getTargetUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        if (validAuthToken(request.getAuthToken()))
        {
            DataPage<Follows> page = strategy.getPage();
            PagedUserResponse finalResponse =
                    new PagedUserResponse(new ArrayList<>(request.getLimit()), page.isHasMorePages());
            for (Follows followsRow : page.getValues())
            {
                User user = strategy.getUser(followsRow);
                finalResponse.getItems().add(user);
            }
            return finalResponse;
        }
        return new PagedUserResponse("Authentication Expired");
    }
    public FollowResponse follow(FollowRequest request)
    {
        if (request.getFollowee() == null)
        {
            throw new RuntimeException("[Bad Request] Request needs to have a followee");
        }
        if (validAuthToken(request.getAuthToken()))
        {
            getFollowDAO().putFollow(request.getFollower(), request.getFollowee());
            getUserDAO().updateFollowersCount(request.getFollowee().getAlias(), '+');
            getUserDAO().updateFollowingCount(request.getFollower().getAlias(), '+');
            return new FollowResponse();
        }
        return new FollowResponse("Authentication Expired: Logout to restore");

    }
    public UnfollowResponse unfollow(UnfollowRequest request)
    {
        if (request.getFollowee() == null)
        {
            throw new RuntimeException("[Bad Request] Request needs to have a followee");
        }
        if (validAuthToken(request.getAuthToken()))
        {
            getFollowDAO().deleteFollow(request.getFollower(), request.getFollowee());
            getUserDAO().updateFollowersCount(request.getFollowee().getAlias(), '-');
            getUserDAO().updateFollowingCount(request.getFollower().getAlias(), '-');
            return new UnfollowResponse();
        }
        return new UnfollowResponse("Authentication Expired");
    }
    private interface GetCountStrategy{
        int getCount(Users usersRow);
    }
    public GetCountResponse getFollowersCount(GetCountRequest request)
    {
        GetCountStrategy strategy = Users::getFollowersCount;
        return getCount(request, strategy);
    }
    public GetCountResponse getFollowingCount(GetCountRequest request)
    {
        GetCountStrategy strategy = Users::getFollowingCount;
        return getCount(request, strategy);
    }
    private GetCountResponse getCount(GetCountRequest request, GetCountStrategy strategy)
    {
        if(request.getTargetUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        }
        if(request.getAuthToken() == null)
        {
            throw new RuntimeException("[Bad Request] No authToken");
        }
        if (validAuthToken(request.getAuthToken()))
        {
            Users usersRow = getUserDAO().getUser(request.getTargetUserAlias());
            int count = strategy.getCount(usersRow);
            return new GetCountResponse(count);
        }
        return new GetCountResponse("Authentication Expired: Logout to restore");
    }
    public IsFollowerResponse isFollower(IsFollowerRequest request)
    {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        }
        else if(request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }
        if (validAuthToken(request.getAuthToken()))
        {
            Follows followsRow = getFollowDAO().getFollow(request.getFollowerAlias(), request.getFolloweeAlias());
            return new IsFollowerResponse(followsRow != null);
        }
        return new IsFollowerResponse("Authentication Expired: Logout to restore");
    }
    public FollowDAO getFollowDAO()
    {
        return getFactory().getFollowDAO();
    }
    public UserDAO getUserDAO()
    {
        return getFactory().getUserDAO();
    }
}
