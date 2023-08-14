package edu.byu.cs.tweeter.server.Lambda.StatusServices;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.PagedStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PagedStatusResponse;

/**
 * An AWS lambda function that returns the statuses for a user story.
 */
public class GetStoryHandler extends StatusServiceHandler implements RequestHandler<PagedStatusRequest, PagedStatusResponse>
{

    /**
     * Returns the statuses that the user specified in the request. Uses information in
     * the request object to limit the number of statuses returned and to return the next set of
     * statuses after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the statuses.
     */
    @Override
    public PagedStatusResponse handleRequest(PagedStatusRequest request, Context context)
    {
        return getStatusService().getStory(request);
    }
}