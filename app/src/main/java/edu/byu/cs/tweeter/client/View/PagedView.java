package edu.byu.cs.tweeter.client.View;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public interface PagedView<T> extends View
{
    void setLoadingFooter(Boolean value);
    void addMoreItems(List<T> items);
}
