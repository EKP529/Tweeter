package edu.byu.cs.tweeter.server.DAO;

import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.AuthTokens;

public interface AuthTokenDAO
{
    AuthTokens getAuthToken(String token);
    void updateAuthTokensTable(Long threshold);
    void deleteAuthToken(String token);
    AuthTokens putAuthToken(String token, String alias);
}
