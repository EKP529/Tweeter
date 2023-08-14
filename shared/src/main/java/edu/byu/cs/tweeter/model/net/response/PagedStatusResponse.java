package edu.byu.cs.tweeter.model.net.response;

import java.util.List;
import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.Status;

public class PagedStatusResponse extends PagedResponse<Status>
{
    public PagedStatusResponse(List<Status> items, boolean hasMorePages)
    {
        super(items, hasMorePages);
    }

    public PagedStatusResponse(String message)
    {
        super(message);
    }

    @Override
    public boolean equals(Object param)
    {
        if (this == param) {
            return true;
        }

        if (param == null || getClass() != param.getClass()) {
            return false;
        }

        PagedStatusResponse that = (PagedStatusResponse) param;

        return (Objects.equals(getItems(), that.getItems()) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getItems());
    }
}

