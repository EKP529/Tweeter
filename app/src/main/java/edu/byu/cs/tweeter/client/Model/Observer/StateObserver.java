package edu.byu.cs.tweeter.client.Model.Observer;

public interface StateObserver<T> extends ServiceObserver
{
    void handleSuccess(T item);
}
