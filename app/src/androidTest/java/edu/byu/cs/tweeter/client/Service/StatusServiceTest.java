package edu.byu.cs.tweeter.client.Service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import edu.byu.cs.tweeter.client.Model.Net.ServerFacade;
import edu.byu.cs.tweeter.client.Model.Observer.LoadingObserver;
import edu.byu.cs.tweeter.client.Model.Service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

public class StatusServiceTest
{
    private User currentUser;
    private StatusService statusServiceSpy;
    private GetStoryObserver observer;
    private CountDownLatch countDownLatch;

    /**
     * Create a StatusService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup()
    {
        currentUser = new User("FirstName", "LastName", null);
        statusServiceSpy = Mockito.spy(new StatusService());

        // Setup an observer for the StatusService
        observer = new GetStoryObserver();

        // Prepare the countdown latch
        resetCountDownLatch();
    }
    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }
    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    /**
     * A {@link LoadingObserver} implementation that can be used to get the values
     * eventually returned by an asynchronous call on the {@link StatusService}. Counts down
     * on the countDownLatch so tests can wait for the background thread to call a method on the
     * observer.
     */
    private class GetStoryObserver implements LoadingObserver<Status>
    {
        private boolean success;
        private String message;
        private List<Status> items;
        private boolean hasMorePages;
        private Exception exception;

        @Override
        public void handleSuccess(List<Status> items, boolean hasMorePages) {
            this.success = true;
            this.message = null;
            this.items = items;
            this.hasMorePages = hasMorePages;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message) {
            this.success = false;
            this.message = message;
            this.items = null;
            this.hasMorePages = false;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleException(Exception exception) {
            this.success = false;
            this.message = null;
            this.items = null;
            this.hasMorePages = false;
            this.exception = exception;

            countDownLatch.countDown();
        }
        public boolean isSuccess() {
            return success;
        }
        public String getMessage() {
            return message;
        }
        public List<Status> getItems() {
            return items;
        }
        public boolean getHasMorePages() {
            return hasMorePages;
        }
        public Exception getException() {
            return exception;
        }
    }

    /**
     * Verify that for successful requests, the {@link StatusService#loadMoreStory}
     * asynchronous method eventually returns the same result as the {@link ServerFacade}.
     */
    @Test
    public void testLoadMoreStory_validRequest_correctResponse() throws InterruptedException
    {
        statusServiceSpy.loadMoreStory(currentUser, 3, null, observer);
        awaitCountDownLatch();

        List<Status> expectedStatuses = FakeData.getInstance().getFakeStatuses().subList(0, 3);
        Assertions.assertTrue(observer.isSuccess());
        Assertions.assertNull(observer.getMessage());
        int i = 0;
        for (Status status : observer.getItems())
        {
            Assertions.assertEquals(expectedStatuses.get(i).getPost(), status.getPost());
            Assertions.assertEquals(expectedStatuses.get(i).getUser(), status.getUser());
            Assertions.assertEquals(expectedStatuses.get(i).getMentions(), status.getMentions());
            Assertions.assertEquals(expectedStatuses.get(i).getUrls(), status.getUrls());
            i++;
        }
        Assertions.assertTrue(observer.getHasMorePages());
        Assertions.assertNull(observer.getException());
    }

    /**
     * Verify that for successful requests, the {@link StatusService#loadMoreStory}
     * method loads the statuses included in the result.
     */
    @Test
    public void testLoadMoreStory_validRequest_loadsStatuses() throws InterruptedException
    {
        statusServiceSpy.loadMoreStory(currentUser, 3, null, observer);
        awaitCountDownLatch();

        List<Status> statuses = observer.getItems();
        Assertions.assertTrue(statuses.size() > 0);
    }

}
