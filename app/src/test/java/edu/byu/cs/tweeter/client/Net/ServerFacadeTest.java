package edu.byu.cs.tweeter.client.Net;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import edu.byu.cs.tweeter.client.Model.Net.ServerFacade;
import edu.byu.cs.tweeter.client.Model.Net.TweeterRequestException;
import edu.byu.cs.tweeter.client.Model.Service.FollowService;
import edu.byu.cs.tweeter.client.Model.Service.UserService;
import edu.byu.cs.tweeter.model.net.request.GetCountRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.PagedUserRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.model.net.response.GetCountResponse;
import edu.byu.cs.tweeter.model.net.response.PagedUserResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class ServerFacadeTest
{
    private ServerFacade serverFacade;
    private RegisterRequest registerRequest;
    private PagedRequest getFollowersRequest;
    private FakeData fakeData;
    String firstName = "Eden";
    String lastName = "Paupulaire";
    String username = "alias";
    String password = "password";
    String image = "image";

    @BeforeEach
    public void setup()
    {
        serverFacade = new ServerFacade();
        fakeData = FakeData.getInstance();
    }

    @Test
    public void registerTest_registerSuccess()
    {
        registerRequest = new RegisterRequest(firstName, lastName, username, password, image);
        try
        {
            AuthenticateResponse response = serverFacade.register(registerRequest, UserService.REGISTER_URL_PATH);
            Assertions.assertTrue(response.isSuccess());
            Assertions.assertNotNull(response.getUser());
            Assertions.assertNotNull(response.getAuthToken());
            Assertions.assertNull(response.getMessage());
            Assertions.assertEquals(fakeData.getFirstUser().getFirstName(), response.getUser().getFirstName());
            Assertions.assertEquals(fakeData.getFirstUser().getLastName(), response.getUser().getLastName());
            Assertions.assertEquals(fakeData.getFirstUser().getAlias(), response.getUser().getAlias());
            Assertions.assertEquals(fakeData.getFirstUser().getImageUrl(), response.getUser().getImageUrl());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void registerTest_registerFailure()
    {
        registerRequest = new RegisterRequest(null, lastName, username, password, image);
        try
        {
            TweeterRequestException ex = Assertions.assertThrows(TweeterRequestException.class, () ->
                    serverFacade.register(registerRequest, UserService.REGISTER_URL_PATH));
            Assertions.assertEquals("[Bad Request] Missing a first name", ex.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getFollowersTest_success()
    {
        getFollowersRequest = new PagedUserRequest(fakeData.getAuthToken(), username, 3, null);
        try
        {
            PagedUserResponse response = serverFacade.getUsers(getFollowersRequest, FollowService.GET_FOLLOWERS_URL_PATH);
            Assertions.assertTrue(response.isSuccess());
            Assertions.assertNull(response.getMessage());
            Assertions.assertTrue(response.getHasMorePages());
            Assertions.assertNotNull(response.getItems());
            Assertions.assertEquals(fakeData.getFakeUsers().subList(0, 3), response.getItems());
            Assertions.assertTrue(response.getHasMorePages());
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void getFollowersTest_failure()
    {
        getFollowersRequest = new PagedRequest(fakeData.getAuthToken(), null, 3, null);
        try
        {
            TweeterRequestException ex = Assertions.assertThrows(TweeterRequestException.class, () ->
                    serverFacade.getUsers(getFollowersRequest, FollowService.GET_FOLLOWERS_URL_PATH));
            Assertions.assertEquals("[Bad Request] Request needs to have a user alias", ex.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getFollowersCountTest_success()
    {
        GetCountRequest getFollowersCountRequest = new GetCountRequest(fakeData.getAuthToken(), username);
        try
        {
            GetCountResponse response = serverFacade.getCount(getFollowersCountRequest, FollowService.GET_FOLLOWERS_COUNT_URL_PATH);
            Assertions.assertTrue(response.isSuccess());
            Assertions.assertNull(response.getMessage());
            Assertions.assertTrue(response.getCount() > 0);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
