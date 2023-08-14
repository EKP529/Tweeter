package edu.byu.cs.tweeter.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.Model.Cache.Cache;
import edu.byu.cs.tweeter.client.Model.Net.ServerFacade;
import edu.byu.cs.tweeter.client.Model.Observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.Model.Service.StatusService;
import edu.byu.cs.tweeter.client.Model.Service.UserService;
import edu.byu.cs.tweeter.client.Presenter.MainPresenter;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.PagedStatusRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.model.net.response.PagedStatusResponse;

public class PostStatusTest
{
    private MainPresenter.MainView mockView;
    private MainPresenter mainPresenterSpy;
    private ServerFacade serverFacade = new ServerFacade();
    private CountDownLatch countDownLatch;
    private String username = "@guy6";
    private String password = "p";
    private String post = "This is a test!";

    @BeforeEach
    public void setup()
    {
        mockView = Mockito.mock(MainPresenter.MainView.class);
        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView));
        Mockito.when(mainPresenterSpy.getPostStatusObserver()).thenReturn(new PostStatusObserver());
        resetCountDownLatch();
    }
    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }
    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }
    private class PostStatusObserver implements SimpleNotificationObserver
    {
        @Override
        public void handleSuccess()
        {
            mockView.displayMessage("Successfully Posted!");
            countDownLatch.countDown();
        }
        @Override
        public void handleFailure(String message)
        {
            mockView.displayMessage("Failed to post the status: " + message);
            countDownLatch.countDown();
        }
        @Override
        public void handleException(Exception ex)
        {
            mockView.displayMessage("Failed to post the status because of exception: " + ex.getMessage());
            countDownLatch.countDown();
        }
    }
    @Test
    public void postStatusTest_postSuccess() throws Exception
    {
        AuthenticateResponse response = serverFacade.login(new LoginRequest(username, password),
                UserService.LOGIN_URL_PATH);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertNotNull(response.getAuthToken());
        Assertions.assertNotNull(response.getUser());
        Assertions.assertEquals(response.getUser().getAlias(), username);
        Cache.getInstance().setCurrUser(response.getUser());
        Cache.getInstance().setCurrUserAuthToken(response.getAuthToken());
        mainPresenterSpy.postStatus(post);
        awaitCountDownLatch();
        Mockito.verify(mockView).displayMessage("Posting Status...");
        Mockito.verify(mockView).displayMessage("Successfully Posted!");
        PagedStatusResponse response1 = serverFacade.getStatuses(
                new PagedStatusRequest(response.getAuthToken(), username, 1, null),
                StatusService.GET_STORY_URL_PATH);
        Assertions.assertEquals(response1.getItems().size(), 1);
        Assertions.assertEquals(response1.getItems().get(0).getPost(), post);
        Assertions.assertEquals(response1.getItems().get(0).getUser().getAlias(), username);
        Assertions.assertEquals(response1.getItems().get(0).getUrls().size(), 0);
        Assertions.assertEquals(response1.getItems().get(0).getMentions().size(), 0);
    }
}
