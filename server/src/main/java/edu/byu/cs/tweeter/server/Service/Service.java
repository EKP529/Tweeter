package edu.byu.cs.tweeter.server.Service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.DAO.AuthTokenDAO;
import edu.byu.cs.tweeter.server.DAO.DAOFactory;
import edu.byu.cs.tweeter.server.DAO.DynamoDB.Model.AuthTokens;

public abstract class Service
{
    protected static final Long EXPIRATION = (long) 120000;
    private DAOFactory factory;
    protected Service(DAOFactory factory)
    {
        this.factory = factory;
    }
    protected DAOFactory getFactory() { return factory;}

    public AuthTokenDAO getAuthTokenDAO() {
        return getFactory().getAuthTokenDAO();
    }

    protected boolean validAuthToken(AuthToken authToken)
    {
        long threshold = System.currentTimeMillis() - authToken.getDatetime();
        if (threshold >= EXPIRATION)
        {
            threshold = System.currentTimeMillis() - EXPIRATION;
//            getFactory().getAuthTokenDAO().updateAuthTokensTable(threshold);
            return false;
        }
        return true;
    }

}
