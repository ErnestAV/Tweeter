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
    }

    private UserService userService;
    private RegisterView view;
    public GetRegisterPresenter(RegisterView view) {
        this.view = view;
        userService = new UserService();
    }

    public void registerTask(String firstName, String lastName, String userAlias, String password, ImageView imageToUpload) {
        // Convert image to byte array.
        Bitmap image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imageBytes = bos.toByteArray();

        // Intentionally, Use the java Base64 encoder so it is compatible with M4.
        String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);

        view.toggleRegisterToast(true);
        userService.registerTask(firstName, lastName, userAlias, password, imageBytesBase64, new RegisterObserver());
    }

    public class RegisterObserver implements UserService.Observer {

        @Override
        public void startActivity(User user) {
            view.startActivity(user);
        }

        @Override
        public void toggleLoginToast(boolean isActive) {

        }

        @Override
        public void displayLoginSuccess(String message) {
            view.displayMessage("Hello " + message);
        }

        @Override
        public void displayLoginError(String message) {
            view.displayMessage("Failed to register: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to register because of exception: " + ex.getMessage());
        }

        @Override
        public void toggleRegisterToast(boolean isActive) {
            view.toggleRegisterToast(isActive);
        }
    }
}
