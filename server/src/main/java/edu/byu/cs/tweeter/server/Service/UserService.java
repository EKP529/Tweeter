package edu.byu.cs.tweeter.server.Service;

import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.AuthenticateRequest;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.server.DAO.DAOFactory;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.AuthTokens;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.Users;
import edu.byu.cs.tweeter.server.DAO.UserDAO;

public class UserService extends Service
{
    public UserService(DAOFactory factory) {
        super(factory);
    }

    public GetUserResponse getUser(GetUserRequest request)
    {
        if (validAuthToken(request.getAuthToken()))
        {
            if (request.getAlias() == null)
                throw new RuntimeException("[Bad Request] Missing an user alias");
            else
            {
                Users usersRow = getUserDAO().getUser(request.getAlias());
                if (usersRow == null)
                    throw new RuntimeException("[Bad Request] Username not found");
                String[] name = usersRow.getName().split(" ");
                User user = new User(name[0], name[1], usersRow.getHandle(), usersRow.getImageURL());
                return new GetUserResponse(user);
            }
        }
        else {
            return new GetUserResponse("Authentication Expired: Logout to restore");
        }

    }
    public LogoutResponse logout(LogoutRequest request)
    {
        getAuthTokenDAO().deleteAuthToken(request.getAuthToken().getToken());
        Long threshold = System.currentTimeMillis() - Service.EXPIRATION;
//        getAuthTokenDAO().updateAuthTokensTable(threshold);
        return new LogoutResponse();
    }
    private interface GetUsersRowStrategy
    {
        Users getUsersRow();
    }
    public AuthenticateResponse login(LoginRequest request)
    {
        GetUsersRowStrategy strategy = () ->
        {
            try {
                Users usersRow = getUserDAO().getUser(request.getUsername());
                if (usersRow == null)
                    throw new RuntimeException("[Bad Request] Username not found");
                String storedPassword = usersRow.getPasswordHash();
                if (!PBKDF2WithHmacSHA1Hashing.validatePassword(request.getPassword(), storedPassword))
                {
                    throw new RuntimeException("[Bad Request] Incorrect password:\n" +
                            "This is the storedPassword: " + storedPassword + "\n" +
                            "This is the originalPaassword: " + request.getPassword() + "\n");
                }
            } catch(Exception ex) {
                throw new RuntimeException("[Server Error] Error processing password or " + ex.getMessage());
            }
            return getUserDAO().getUser(request.getUsername());
        };
        return authenticate(request, strategy);
    }
    public AuthenticateResponse register(RegisterRequest request)
    {
        GetUsersRowStrategy strategy = () ->
        {
            if(request.getFirstName() == null) {
                throw new RuntimeException("[Bad Request] Missing a first name");
            }else if(request.getLastName() == null) {
                throw new RuntimeException("[Bad Request] Missing a last name");
            }else if(request.getImage() == null) {
                throw new RuntimeException("[Bad Request] Missing an image");
            }
            String passwordHash;
            try {
                 passwordHash = PBKDF2WithHmacSHA1Hashing
                        .generateStrongPasswordHash(request.getPassword());
            } catch(Exception ex) {
                throw new RuntimeException("[Server Error] Error processing password or " + ex.getMessage());
            }
            User user = new User(request.getFirstName(), request.getLastName(), request.getUsername(), request.getImage());
            return getUserDAO().putUser(user, passwordHash);
        };
        return authenticate(request, strategy);
    }
    private AuthenticateResponse authenticate(AuthenticateRequest request,
                                                           GetUsersRowStrategy strategy)
    {
        if(request.getUsername() == null) {
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }
        Users usersRow = strategy.getUsersRow();
        String[] name = usersRow.getName().split(" ");
        User user = new User(name[0], name[1], usersRow.getHandle(), usersRow.getImageURL());
        AuthTokens authTokensRow = getAuthTokenDAO()
                .putAuthToken(UUID.randomUUID().toString(), user.getAlias());
        AuthToken authToken =
                new AuthToken(authTokensRow.getAuthToken(), authTokensRow.getDateTime());
        return new AuthenticateResponse(user, authToken);
    }
    public UserDAO getUserDAO() { return getFactory().getUserDAO();}
}
