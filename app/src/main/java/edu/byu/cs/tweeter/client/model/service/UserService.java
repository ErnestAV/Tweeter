package edu.byu.cs.tweeter.client.model.service;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.RegisterTask;
import edu.byu.cs.tweeter.client.presenter.GetLoginPresenter;
import edu.byu.cs.tweeter.client.presenter.GetRegisterPresenter;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {

    public interface Observer {

        void startActivity(User user);

        void toggleLoginToast(boolean isActive);

        void displayLoginSuccess(String message);

        void displayLoginError(String message);

        void displayException(Exception ex);

        void toggleRegisterToast(boolean isActive);
    }

    public void loginTask(String userAlias, String userPassword, GetLoginPresenter.LoginObserver loginObserver) {
        // Send the login request.
        LoginTask loginTask = new LoginTask(userAlias,userPassword,
                new LoginHandler(loginObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(loginTask);
    }

    public void registerTask(String firstName, String lastName, String userAlias, String password, String imageBytesBase64, GetRegisterPresenter.RegisterObserver registerObserver) {
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

        Observer observer;
        public LoginHandler(Observer observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
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

                observer.startActivity(loggedInUser);
                observer.toggleLoginToast(false);
                observer.displayLoginSuccess(Cache.getInstance().getCurrUser().getName());
            } else if (msg.getData().containsKey(LoginTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(LoginTask.MESSAGE_KEY);
                observer.displayLoginError(message);
            } else if (msg.getData().containsKey(LoginTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(LoginTask.EXCEPTION_KEY);
                observer.displayException(ex);
            }
        }
    }

    // RegisterHandler

    private class RegisterHandler extends Handler {

        Observer observer;
        public RegisterHandler(Observer observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
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
                    observer.startActivity(registeredUser);
                    observer.toggleRegisterToast(false);
                    observer.displayLoginSuccess(Cache.getInstance().getCurrUser().getName());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else if (msg.getData().containsKey(RegisterTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(RegisterTask.MESSAGE_KEY);
                observer.displayLoginError(message);
            } else if (msg.getData().containsKey(RegisterTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(RegisterTask.EXCEPTION_KEY);
                observer.displayException(ex);
            }
        }
    }
}
