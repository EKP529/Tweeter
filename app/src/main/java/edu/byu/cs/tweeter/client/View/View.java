package edu.byu.cs.tweeter.client.View;

import edu.byu.cs.tweeter.model.domain.User;

public interface View
{
    void displayMessage(String message);
    void showUser(User user);
}
