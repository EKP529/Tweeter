package edu.byu.cs.tweeter.client.Model.Net;

import java.io.IOException;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.GetCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.PagedStatusRequest;
import edu.byu.cs.tweeter.model.net.request.PagedUserRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.GetCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.PagedStatusResponse;
import edu.byu.cs.tweeter.model.net.response.PagedUserResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade
{
    // TODO: Set this to the invoke URL of your API. Find it by going to your API in AWS, clicking
    //  on stages in the right-side menu, and clicking on the stage you deployed your API to.
    private static final String SERVER_URL = "https://19qa8fgvpb.execute-api.us-west-2.amazonaws.com/Dev";
    private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);

    public GetCountResponse getCount(GetCountRequest request, String urlPath) throws IOException, TweeterRemoteException
    {
        return clientCommunicator.doPost(urlPath, request, null, GetCountResponse.class);
    }
    public IsFollowerResponse isFollower(IsFollowerRequest request, String urlPath) throws IOException, TweeterRemoteException
    {
        return clientCommunicator.doPost(urlPath, request, null, IsFollowerResponse.class);
    }
    public GetUserResponse getUser(GetUserRequest request, String urlPath) throws IOException, TweeterRemoteException
    {
        return clientCommunicator.doPost(urlPath, request, null, GetUserResponse.class);
    }
    public PostStatusResponse postStatus(PostStatusRequest request, String urlPath)
            throws IOException, TweeterRemoteException
    {
        return clientCommunicator.doPost(urlPath, request, null, PostStatusResponse.class);
    }
    /**
     * Performs a login and if successful, returns the logged in user and an auth token.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    public AuthenticateResponse login(LoginRequest request, String urlPath)
            throws IOException, TweeterRemoteException
    {
        return clientCommunicator.doPost(urlPath, request, null, AuthenticateResponse.class);
    }
    public AuthenticateResponse register(RegisterRequest request, String urlPath)
            throws IOException, TweeterRemoteException
    {
        return clientCommunicator.doPost(urlPath, request, null, AuthenticateResponse.class);
    }

    /**
     * Returns the followees/followers that the user specified in the request. Uses information in
     * the request object to limit the number of followees/followers returned and to return the
     * next set of followees/followers after any that were returned in a previous request.
     * @param request contains information about the user whose followees/followers are to be
     *                returned and any other information required to satisfy the request.
     * @return the followees.
     */
    public PagedUserResponse getUsers(PagedUserRequest request, String urlPath)
            throws IOException, TweeterRemoteException
    {
        return clientCommunicator.doPost(urlPath, request, null, PagedUserResponse.class);
    }
    public PagedStatusResponse getStatuses(PagedStatusRequest request, String urlPath)
            throws IOException, TweeterRemoteException
    {
        return clientCommunicator.doPost(urlPath, request, null, PagedStatusResponse.class);
    }
    public FollowResponse follow(FollowRequest request, String urlPath)
            throws IOException, TweeterRemoteException
    {
        return clientCommunicator.doPost(urlPath, request, null, FollowResponse.class);
    }
    public UnfollowResponse unfollow(UnfollowRequest request, String urlPath)
            throws IOException, TweeterRemoteException
    {
        return clientCommunicator.doPost(urlPath, request, null, UnfollowResponse.class);
    }
    public LogoutResponse logout(LogoutRequest request, String urlPath)
            throws IOException, TweeterRemoteException
    {
        return clientCommunicator.doPost(urlPath, request, null, LogoutResponse.class);
    }
}
