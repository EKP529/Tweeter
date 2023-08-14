package edu.byu.cs.tweeter.client.Presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.ArgumentMatchers.*;
import edu.byu.cs.tweeter.client.Model.Observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.Model.Service.StatusService;
import edu.byu.cs.tweeter.client.Model.Cache.Cache;
import edu.byu.cs.tweeter.model.domain.Status;

public class MainPresenterUnitTest
{
    private MainPresenter.MainView mockView;
    private StatusService mockStatusService;
    private MainPresenter mainPresenterSpy;
    private Cache mockCache;
    private MainPresenter.PostStatusObserver observer;
    private final String post = "Hello";

    @BeforeEach
    public void setup()
    {
        mockView = Mockito.mock(MainPresenter.MainView.class);
        mockStatusService = Mockito.mock(StatusService.class);
        mockCache = Mockito.mock(Cache.class);
        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView));
        Mockito.when(mainPresenterSpy.getStatusService()).thenReturn(mockStatusService);
        Cache.setInstance(mockCache);
    }

    @Test
    public void postStatusTest_postSuccess()
    {
        Answer<Void> answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                verifyParams(invocation);
                observer.handleSuccess();
                return null;
            }
        };
        verifyPostStatus(answer, "Successfully Posted!");
    }

//    @Test
//    public void postStatusTest_postFailure()
//    {
//        Answer<Void> answer = invocation ->
//        {
//            verifyParams(invocation);
//            observer.handleFailure("Testing Failure");
//            return null;
//        };
//        verifyPostStatus(answer, "Failed to post the status: Testing Failure");
//    }

//    @Test
//    public void postStatusTest_postException()
//    {
//        Answer<Void> answer = invocation ->
//        {
//            verifyParams(invocation);
//            observer.handleException(new Exception("Testing Exception"));
//            return null;
//        };
//        verifyPostStatus(answer, "Failed to post the status because of exception: Testing Exception");
//    }
    public void verifyParams(InvocationOnMock invoc)
    {
        Status status = invoc.getArgument(0, Status.class);
        Assertions.assertEquals(status.getPost(), post);
        observer = invoc.getArgument(1, MainPresenter.PostStatusObserver.class);
    }
    public void verifyPostStatus(Answer<Void> answer, String message)
    {
        Mockito.doAnswer(answer).when(mockStatusService).postStatus(any(Status.class), any(SimpleNotificationObserver.class));
        mainPresenterSpy.postStatus(post);
        Mockito.verify(mockView).displayMessage("Posting Status...");
        Mockito.verify(mockCache).getCurrUser();
        Mockito.verify(mockStatusService).postStatus(any(Status.class), any(SimpleNotificationObserver.class));
        Mockito.verify(mockView).displayMessage(message);
    }
}
