package edu.byu.cs.tweeter.client.Model.Observer;

import java.util.List;

public interface LoadingObserver<T> extends ServiceObserver
{
    void handleSuccess(List<T> items, boolean hasMorePages);
}
