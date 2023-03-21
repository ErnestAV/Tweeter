package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class GetRegisterPresenter {

    public interface RegisterView {
        void startActivity(User registeredUser);

        void toggleRegisterToast(boolean isActive);

        void displayMessage(String message);

        void setErrorMessage(String message);
    }

    private UserService userService;
    private RegisterView registerView;
    public GetRegisterPresenter(RegisterView registerView) {
        this.registerView = registerView;
        userService = new UserService();
    }

    public void registerTask(String firstName, String lastName, String userAlias, String password, ImageView imageToUpload) {
        try {
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

            if (imageToUpload.getDrawable() == null) {
                throw new IllegalArgumentException("Profile image must be uploaded.");
            }

            registerView.setErrorMessage(null);
            // Convert image to byte array.
            Bitmap image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] imageBytes = bos.toByteArray();

            // Intentionally, Use the java Base64 encoder so it is compatible with M4.
            String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);

            registerView.toggleRegisterToast(true);
            userService.registerTask(firstName, lastName, userAlias, password, imageBytesBase64, new RegisterUserServiceObserver());
        } catch (Exception e) {
            registerView.setErrorMessage(e.getMessage());
        }
    }

    public class RegisterUserServiceObserver implements UserService.UserServiceObserver {

        @Override
        public void startActivity(User user) {
            registerView.startActivity(user);
        }

        @Override
        public void displaySuccess(String message) {
            registerView.displayMessage("Hello " + message);
        }

        @Override
        public void displayError(String message) {
            registerView.displayMessage("Failed to register: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            registerView.displayMessage("Failed to register because of exception: " + ex.getMessage());
        }

        @Override
        public void toggleToast(boolean isActive) {
            registerView.toggleRegisterToast(isActive);
        }
    }
}
