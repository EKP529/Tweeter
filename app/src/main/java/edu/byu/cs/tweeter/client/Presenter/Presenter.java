package edu.byu.cs.tweeter.client.Presenter;

import edu.byu.cs.tweeter.client.Model.Service.UserService;
import edu.byu.cs.tweeter.client.View.View;

public abstract class Presenter<T extends View>
{
    protected final T view;
    protected final UserService userService;
    public Presenter(T view)
    {
        this.userService = new UserService();
        this.view = view;
    }
}
