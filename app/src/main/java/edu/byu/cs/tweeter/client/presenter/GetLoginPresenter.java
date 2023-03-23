package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.view.BaseView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetLoginPresenter extends Presenter {

    public interface View extends BaseView {
        void displayErrorMessage(String message);
        void clearErrorMessage();
        void navigateToUser(User user);
    }

    public GetLoginPresenter(GetLoginPresenter.View view) {
        super(view);
    }

    public void loginTask(String userAlias, String userPassword) {
        String message = validateLogin(userAlias, userPassword);

        if (message == null) {
            ((View) view).clearErrorMessage();
            view.displayMessage("Logging in...");
            new UserService().loginTask(userAlias, userPassword, new LoginObserver());
        } else {
            view.clearMessage();
            ((View) view).displayErrorMessage(message);
        }
    }

    public String validateLogin(String userAlias, String userPassword) {
        if (userAlias.length() > 0 && userAlias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (userAlias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (userPassword.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        return null;
    }

    private class LoginObserver implements UserService.LoginObserver {

        @Override
        public void handleSuccess(User user, AuthToken authToken) {
            Cache.getInstance().setCurrUser(user);
            Cache.getInstance().setCurrUserAuthToken(authToken);
            ((View) view).displayMessage("Hello " + user.getFirstName());
            ((View) view).clearErrorMessage();
            ((View) view).navigateToUser(user);
        }

        @Override
        public void handleFailure(String message) {
            ((View) view).clearErrorMessage();
            ((View) view).displayErrorMessage("Could not login: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            ((View) view).clearErrorMessage();
            ((View) view).displayErrorMessage("Could not login due to exception: " + exception.getMessage());
        }
    }

//    public interface LoginView {
//
//        void startActivity(User loggedInUser);
//
//        void toggleLoginToast(boolean isActive);
//
//        void displayMessage(String message);
//
//        void setErrorMessage(String message);
//    }
//
//    private UserService userService;
//
//    private LoginView loginView;
//
//    public GetLoginPresenter(LoginView loginView) {
//        this.loginView = loginView;
//        userService = new UserService();
//    }
//
//    public void loginTask(String userAlias, String userPassword) {
//        try {
//            if (userAlias.length() > 0 && userAlias.charAt(0) != '@') {
//                throw new IllegalArgumentException("Alias must begin with @.");
//            }
//            if (userAlias.length() < 2) {
//                throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
//            }
//            if (userPassword.length() == 0) {
//                throw new IllegalArgumentException("Password cannot be empty.");
//            }
//            loginView.setErrorMessage(null);
//            loginView.toggleLoginToast(true);
//            userService.loginTask(userAlias, userPassword, new LoginUserServiceObserver());
//        } catch (Exception e) {
//            loginView.setErrorMessage(e.getMessage());
//        }
//    }
//
//    public class LoginUserServiceObserver implements UserService.UserServiceObserver {
//
//        @Override
//        public void startActivity(User user) {
//            loginView.startActivity(user);
//        }
//
//        @Override
//        public void displaySuccess(String message) {
//            loginView.displayMessage("Hello " + message);
//        }
//
//        @Override
//        public void displayError(String message) {
//            loginView.displayMessage("Failed to login: " + message);
//        }
//
//        @Override
//        public void displayException(Exception ex) {
//            loginView.displayMessage("Failed to login because of exception: " + ex.getMessage());
//        }
//
//        @Override
//        public void toggleToast(boolean isActive) {
//            loginView.toggleLoginToast(isActive);
//        }
//    }
}
