package edu.byu.cs.tweeter.client.Presenter;

import edu.byu.cs.tweeter.client.Model.Observer.StateObserver;
import edu.byu.cs.tweeter.client.Model.Service.UserService;
import edu.byu.cs.tweeter.client.View.View;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter extends Presenter<View>
{
    public LoginPresenter(View view)
    {
        super(view);
    }
    public void validateLogin(String userAlias, String password)
    {
        if (userAlias.length() > 0 && userAlias.charAt(0) != '@')
        {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (userAlias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0)
        {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }
    public void loginUser(String userAlias, String password)
    {
        userService.loginUser(userAlias, password, new LoginObserver());
    }
    private class LoginObserver implements StateObserver<User>
    {
        @Override
        public void handleSuccess(User item)
        {
            view.showUser(item);
        }
        @Override
        public void handleFailure(String message)
        {
            view.displayMessage("Failed to login: " + message);
        }
        @Override
        public void handleException(Exception ex)
        {
            view.displayMessage("Failed to login because of exception: " + ex.getMessage());
        }
    }
}
