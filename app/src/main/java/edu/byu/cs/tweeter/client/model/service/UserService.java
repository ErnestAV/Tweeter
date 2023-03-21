package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.RegisterTask;
import edu.byu.cs.tweeter.client.presenter.GetLoginPresenter;
import edu.byu.cs.tweeter.client.presenter.GetRegisterPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {

    public interface UserServiceObserver {

        void startActivity(User user);

        void displaySuccess(String message);

        void displayError(String message);

        void displayException(Exception ex);

        void toggleToast(boolean isActive);
    }

    public void loginTask(String userAlias, String userPassword, GetLoginPresenter.LoginUserServiceObserver loginObserver) {
        // Send the login request.
        LoginTask loginTask = new LoginTask(userAlias,userPassword,
                new LoginHandler(loginObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(loginTask);
    }

    public void registerTask(String firstName, String lastName, String userAlias, String password, String imageBytesBase64, GetRegisterPresenter.RegisterUserServiceObserver registerObserver) {
        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstName, lastName, userAlias, password,
                imageBytesBase64, new RegisterHandler(registerObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(registerTask);
    }

    /**
     * Message handler (i.e., observer) for LoginTask
     */
    private class LoginHandler extends Handler {

        UserServiceObserver userServiceObserver;
        public LoginHandler(UserServiceObserver userServiceObserver) {
            super(Looper.getMainLooper());
            this.userServiceObserver = userServiceObserver;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(LoginTask.SUCCESS_KEY);
            if (success) {
                User loggedInUser = (User) msg.getData().getSerializable(LoginTask.USER_KEY);
                AuthToken authToken = (AuthToken) msg.getData().getSerializable(LoginTask.AUTH_TOKEN_KEY);

                // Cache user session information
                Cache.getInstance().setCurrUser(loggedInUser);
                Cache.getInstance().setCurrUserAuthToken(authToken);

                userServiceObserver.startActivity(loggedInUser);
                userServiceObserver.toggleToast(false);
                userServiceObserver.displaySuccess(Cache.getInstance().getCurrUser().getName());
            } else if (msg.getData().containsKey(LoginTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(LoginTask.MESSAGE_KEY);
                userServiceObserver.displayError(message);
            } else if (msg.getData().containsKey(LoginTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(LoginTask.EXCEPTION_KEY);
                userServiceObserver.displayException(ex);
            }
        }
    }

    // RegisterHandler

    private class RegisterHandler extends Handler {

        UserServiceObserver userServiceObserver;
        public RegisterHandler(UserServiceObserver userServiceObserver) {
            super(Looper.getMainLooper());
            this.userServiceObserver = userServiceObserver;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(RegisterTask.SUCCESS_KEY);
            if (success) {
                User registeredUser = (User) msg.getData().getSerializable(RegisterTask.USER_KEY);
                AuthToken authToken = (AuthToken) msg.getData().getSerializable(RegisterTask.AUTH_TOKEN_KEY);

                // Cache user session information
                Cache.getInstance().setCurrUser(registeredUser);
                Cache.getInstance().setCurrUserAuthToken(authToken);

                try {
                    userServiceObserver.startActivity(registeredUser);
                    userServiceObserver.toggleToast(false);
                    userServiceObserver.displaySuccess(Cache.getInstance().getCurrUser().getName());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (msg.getData().containsKey(RegisterTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(RegisterTask.MESSAGE_KEY);
                userServiceObserver.displayError(message);
            } else if (msg.getData().containsKey(RegisterTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(RegisterTask.EXCEPTION_KEY);
                userServiceObserver.displayException(ex);
            }
        }
    }
}
