package edu.byu.cs.tweeter.client.presenter;

import android.content.Intent;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.LoginTask;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.User;

public class GetLoginPresenter {

    public interface View {

        void startActivity(User loggedInUser);

        void toggleLoginToast(boolean isActive);

        void displayMessage(String message);

        void setErrorMessage(String message);
    }

    private UserService userService;

    private View view;

    public GetLoginPresenter(View view) {
        this.view = view;
        userService = new UserService();
    }

    public void loginTask(String userAlias, String userPassword) {
        try {
            if (userAlias.length() > 0 && userAlias.charAt(0) != '@') {
                throw new IllegalArgumentException("Alias must begin with @.");
            }
            if (userAlias.length() < 2) {
                throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
            }
            if (userPassword.length() == 0) {
                throw new IllegalArgumentException("Password cannot be empty.");
            }
            view.setErrorMessage(null);
            userService.loginTask(userAlias, userPassword, new LoginObserver());
        } catch (Exception e) {
            view.setErrorMessage(e.getMessage());
        }
    }

    public class LoginObserver implements UserService.Observer {

        @Override
        public void startActivity(User loggedInUser) {
            view.startActivity(loggedInUser);
        }

        @Override
        public void toggleLoginToast(boolean isActive) {
            view.toggleLoginToast(isActive);
        }

        @Override
        public void displayLoginSuccess(String message) {
            view.displayMessage("Hello " + message);
        }

        @Override
        public void displayLoginError(String message) {
            view.displayMessage("Failed to login: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to login because of exception: " + ex.getMessage());
        }
    }
}
