package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.view.BaseView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetRegisterPresenter extends Presenter {
    public GetRegisterPresenter(GetRegisterPresenter.View view) {
        super(view);
    }

    public interface View extends BaseView {
        void displayErrorMessage(String message);
        void clearErrorMessage();
        void navigateToUser(User user);
    }

    public String validateRegistration(String firstName, String lastName, String userAlias, String password, String imageToUpload) {
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
        return null;
    }

    public void registerTask(String firstName, String lastName, String userAlias, String password, String imageToUpload) {
        String message = validateRegistration(firstName, lastName, userAlias, password, imageToUpload);

        if (message == null) {
            ((View) view).clearErrorMessage();
            ((View) view).displayMessage("Registering...");

            new UserService().registerTask(firstName, lastName, userAlias, password, imageToUpload, new RegisterObserver());
        } else {
            view.clearMessage();
            ((GetRegisterPresenter.View) view).displayErrorMessage(message);
        }
    }

    private class RegisterObserver implements UserService.RegisterObserver {

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
            ((View) view).displayErrorMessage("Could not register: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            ((View) view).clearErrorMessage();
            ((View) view).displayErrorMessage("Could not register due to exception: "+ exception.getMessage());
        }
    }
}
