package edu.byu.cs.tweeter.client.Model.Observer;

public interface ServiceObserver
{
    void handleFailure(String message);
    void handleException(Exception exception);
}
