package edu.byu.cs.tweeter.client.Model.Service;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler.GetUserHandler;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.GetUserTask;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler.LoginHandler;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler.LogoutHandler;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Handler.RegisterHandler;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.LoginTask;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.LogoutTask;
import edu.byu.cs.tweeter.client.Model.BackgroundTask.Task.RegisterTask;
import edu.byu.cs.tweeter.client.Model.Observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.Model.Observer.StateObserver;
import edu.byu.cs.tweeter.model.domain.User;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class UserService extends Service
{
    public static final String LOGIN_URL_PATH = "/login";
    public static final String REGISTER_URL_PATH = "/register";
    public static final String LOGOUT_URL_PATH = "/logout";
    public static final String GET_USER_URL_PATH = "/getuser";
    public void displayUser(String userAlias, StateObserver<User> observer)
    {
        GetUserTask getUserTask = new GetUserTask(getAuthToken(),
                userAlias, new GetUserHandler(observer));
        executeTask(getUserTask);
    }
    public void loginUser(String userAlias, String password, StateObserver<User> observer)
    {
        LoginTask loginTask = new LoginTask(userAlias, password, new LoginHandler(observer));
        executeTask(loginTask);
    }
    public void registerUser(String firstName, String lastName, String userAlias,
                             String password, Drawable imageToUpload, StateObserver<User> observer)
    {
        // Convert image to byte array.
        Bitmap image = ((BitmapDrawable) imageToUpload).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imageBytes = bos.toByteArray();

        // Intentionally, Use the java Base64 encoder so it is compatible with M4.
        String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);

        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                userAlias, password, imageBytesBase64, new RegisterHandler(observer));

        executeTask(registerTask);
    }
    public void logoutUser(SimpleNotificationObserver observer)
    {
        LogoutTask logoutTask = new LogoutTask(getAuthToken(), new LogoutHandler(observer));
        executeTask(logoutTask);
    }
}
