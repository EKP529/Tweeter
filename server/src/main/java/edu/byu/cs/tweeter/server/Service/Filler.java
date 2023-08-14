package edu.byu.cs.tweeter.server.Service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.DAO.DAOFactory;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.DynamoDBDAOFactory;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.Follows;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.Users;
import edu.byu.cs.tweeter.server.DAO.FollowDAO;
import edu.byu.cs.tweeter.server.DAO.UserDAO;

public class Filler extends Service
{
    private final static int NUM_USERS = 1;
    private final static String FOLLOW_TARGET_ALIAS = "@ep";
    private final static String FOLLOW_TARGET_NAME = "e p";
    private final static String FOLLOW_TARGET_IMAGE_URL = "https://ekptweeterprofileimages.s3.us-west-2.amazonaws.com/@ep";
    private final static  String FOLLOWER_IMAGE_URL = "https://ekptweeterprofileimages.s3.us-west-2.amazonaws.com/@a";
    private static final List<Follows> followsRows = new ArrayList<>();
    private static final List<Users> usersRows = new ArrayList<>();

    protected Filler(DAOFactory factory) {
        super(factory);
    }

    public static void fillDatabase(DAOFactory factory)
    {
        Filler filler = new Filler(factory);
        UserDAO userDAO = filler.getFactory().getUserDAO();
        FollowDAO followDAO = filler.getFactory().getFollowDAO();

        for (int i = NUM_USERS; i <= NUM_USERS + 999; i++) {

            String name = "Guy " + i;
            String alias = "@guy" + i;

            Users userRow = new Users();
            String passHash = null;
            try {
                passHash = PBKDF2WithHmacSHA1Hashing.generateStrongPasswordHash("p");
            }
            catch (Exception ex) { System.out.println(ex.getMessage());}
            userRow.setPasswordHash(passHash);
            userRow.setName(name);
            userRow.setImageURL(FOLLOWER_IMAGE_URL);
            userRow.setHandle(alias);
            userRow.setFollowingCount(1);
            userRow.setFollowersCount(0);
            usersRows.add(userRow);

            Follows followsRow = new Follows();
            followsRow.setFollowee_imageURL(FOLLOW_TARGET_IMAGE_URL);
            followsRow.setFollowee_name(FOLLOW_TARGET_NAME);
            followsRow.setFollowee_handle(FOLLOW_TARGET_ALIAS);
            followsRow.setFollower_handle(alias);
            followsRow.setFollower_name(name);
            followsRow.setFollower_imageURL(FOLLOWER_IMAGE_URL);
            followsRows.add(followsRow);
        }
        if (usersRows.size() > 0) {
            userDAO.putUserBatch(usersRows);
        }
        if (followsRows.size() > 0) {
            followDAO.putFollowBatch(followsRows);
        }
    }

    public static void main(String[] args)
    {
        Filler.fillDatabase(new DynamoDBDAOFactory());
//        Filler filler = new Filler(new DynamoDBDAOFactory());
//        filler.getFactory().getFollowDAO().clearTable(followsRows);
//        filler.getFactory().getUserDAO().clearTable(usersRows);
    }
}
