package edu.byu.cs.tweeter.client.Presenter;

import android.graphics.drawable.Drawable;

import edu.byu.cs.tweeter.client.Model.Observer.StateObserver;
import edu.byu.cs.tweeter.client.Model.Service.UserService;
import edu.byu.cs.tweeter.client.View.View;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter extends Presenter<View>
{
    public RegisterPresenter(View view)
    {
        super(view);
    }
    public void registerUser(String firstName, String lastName, String userAlias,
                                    String password, Drawable imageToUpload)
    {
        userService.registerUser(firstName, lastName, userAlias, password, imageToUpload, new RegisterObserver());
    }
    public void validateRegistration(String firstName, String lastName,
                                     String userAlias, String password, Drawable imageToUpload)
    {
        if (firstName.length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (userAlias.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (userAlias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (userAlias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        if (imageToUpload == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }
    public class RegisterObserver implements StateObserver<User>
    {
        @Override
        public void handleSuccess(User item)
        {
            view.showUser(item);
        }
        @Override
        public void handleFailure(String message)
        {
            view.displayMessage("Failed to register: " + message);
        }
        @Override
        public void handleException(Exception ex)
        {
            view.displayMessage("Failed to register because of exception: " + ex.getMessage());
        }
    }
}
