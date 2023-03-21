package edu.byu.cs.tweeter.client.presenter;

import java.util.List;
import java.util.concurrent.ExecutorService;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class GetMainPresenter {

    public interface MainView {

        void displayFollowingButton();

        void displayFollowButton();

        void displayMessage(String message);

        void updateSelectedUserFollowingAndFollowersAndButton(boolean value);

        void enableFollowButton(boolean value);

        void toggleLogoutToast(boolean isActive);

        void setFollowerCount(int count);

        void setFollowingCount(int count);

        void togglePostingToast(boolean isActive);
    }

    private MainView view;
    private FollowService followService;

    private StatusService statusService;

    public GetMainPresenter(MainView view) {
        this.view = view;
        followService = new FollowService();
        statusService = new StatusService();
    }

    public void isFollowerTask(User user, User selectedUser) {
        followService.getIsFollowerTask(user, selectedUser, new GetIsFollowerObserver());
    }

    public void unfollowTask(User selectedUser) {
        followService.unfollowTask(selectedUser, new UnfollowObserver());
        view.displayMessage("Removing " + selectedUser.getName() + "...");
    }

    public void logoutTask() {
        followService.logoutTask(new LogoutObserver());
        view.toggleLogoutToast(true);
    }

    public void getFollowersCountTask(User selectedUser, ExecutorService executor) {
        followService.followersCountTask(selectedUser, executor, new GetFollowerCountObserver());
    }

    public void getFollowingCountTask(User selectedUser, ExecutorService executor) {
        followService.followingCountTask(selectedUser, executor, new GetFollowingCountObserver());
    }

    public void followTask(User selectedUser) {
        followService.followTask(selectedUser, new FollowObserver());
        view.displayMessage("Adding " + selectedUser.getName() + "...");
    }

    public void getStatusTask(Status newStatus) {
        statusService.getStatusTask(newStatus, new GetStatusObserver());
        view.togglePostingToast(true);
    }

    public class GetIsFollowerObserver implements FollowService.Observer {
        @Override
        public void displayError(String message) {
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to determine following relationship because of exception: " + ex.getMessage());
        }

        @Override
        public void addFollowees(List<User> followees, boolean hasMorePages) {
            // Empty
        }

        @Override
        public void addFollowers(List<User> followers, boolean hasMorePages) {
            // Empty
        }

        @Override
        public void startActivity(User user) {
            // Not yet empty
        }

        @Override
        public void displayFollowingButton() {
            view.displayFollowingButton();
        }

        @Override
        public void displayFollowButton() {
            view.displayFollowButton();
        }

        @Override
        public void updateUserFollowingAndFollowersAndButton(boolean b) {
            // Empty
        }

        @Override
        public void enableFollowButton(boolean value) {
            // Empty
        }

        @Override
        public void toggleLogoutToast(boolean value) {
            // Empty
        }

        @Override
        public void setFollowersCount(int count) {
            // Empty
        }

        @Override
        public void setFolloweesCount(int count) {
            // Empty
        }
    }

    public class UnfollowObserver implements FollowService.Observer {

        @Override
        public void displayError(String message) {
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to unfollow because of exception: " + ex.getMessage());
        }

        @Override
        public void addFollowees(List<User> followees, boolean hasMorePages) {
            // Empty
        }

        @Override
        public void addFollowers(List<User> followers, boolean hasMorePages) {
            // Empty
        }

        @Override
        public void startActivity(User user) {
            // Empty
        }

        @Override
        public void displayFollowingButton() {
            // Empty
        }

        @Override
        public void displayFollowButton() {
            // Empty
        }

        @Override
        public void updateUserFollowingAndFollowersAndButton(boolean value) {
            view.updateSelectedUserFollowingAndFollowersAndButton(value);
        }

        @Override
        public void enableFollowButton(boolean value) {
            view.enableFollowButton(value);
        }

        @Override
        public void toggleLogoutToast(boolean value) {
            // Empty
        }

        @Override
        public void setFollowersCount(int count) {
            // Empty
        }

        @Override
        public void setFolloweesCount(int count) {
            // Empty
        }
    }

    public class LogoutObserver implements FollowService.Observer {

        @Override
        public void displayError(String message) {
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to logout because of exception: " + ex.getMessage());
        }

        @Override
        public void addFollowees(List<User> followees, boolean hasMorePages) {
            // Empty
        }

        @Override
        public void addFollowers(List<User> followers, boolean hasMorePages) {
            // Empty
        }

        @Override
        public void startActivity(User user) {
            // Empty
        }

        @Override
        public void displayFollowingButton() {
            // Empty
        }

        @Override
        public void displayFollowButton() {
            // Empty
        }

        @Override
        public void updateUserFollowingAndFollowersAndButton(boolean b) {
            // Empty
        }

        @Override
        public void enableFollowButton(boolean value) {
            // Empty
        }

        @Override
        public void toggleLogoutToast(boolean value) {
            view.toggleLogoutToast(value);
        }

        @Override
        public void setFollowersCount(int count) {
            // Empty
        }

        @Override
        public void setFolloweesCount(int count) {
            // Empty
        }
    }

    public class GetFollowerCountObserver implements FollowService.Observer {

        @Override
        public void displayError(String message) {
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to get followers count because of exception: " + ex.getMessage());
        }

        @Override
        public void addFollowees(List<User> followees, boolean hasMorePages) {
            // Empty
        }

        @Override
        public void addFollowers(List<User> followers, boolean hasMorePages) {
            // Empty
        }

        @Override
        public void startActivity(User user) {
            // Empty
        }

        @Override
        public void displayFollowingButton() {
            // Empty
        }

        @Override
        public void displayFollowButton() {
            // Empty
        }

        @Override
        public void updateUserFollowingAndFollowersAndButton(boolean b) {
            // Empty
        }

        @Override
        public void enableFollowButton(boolean value) {
            // Empty
        }

        @Override
        public void toggleLogoutToast(boolean value) {
            // Empty
        }

        @Override
        public void setFollowersCount(int count) {
            view.setFollowerCount(count);
        }

        @Override
        public void setFolloweesCount(int count) {
            // Empty
        }
    }

    public class GetFollowingCountObserver implements FollowService.Observer {

        @Override
        public void displayError(String message) {
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to get following count because of exception: " + ex.getMessage());
        }

        @Override
        public void addFollowees(List<User> followees, boolean hasMorePages) {
            // Empty
        }

        @Override
        public void addFollowers(List<User> followers, boolean hasMorePages) {
            // Empty
        }

        @Override
        public void startActivity(User user) {
            // Empty
        }

        @Override
        public void displayFollowingButton() {
            // Empty
        }

        @Override
        public void displayFollowButton() {
            // Empty
        }

        @Override
        public void updateUserFollowingAndFollowersAndButton(boolean b) {
            // Empty
        }

        @Override
        public void enableFollowButton(boolean value) {
            // Empty
        }

        @Override
        public void toggleLogoutToast(boolean value) {
            // Empty
        }

        @Override
        public void setFollowersCount(int count) {
            // Empty
        }

        @Override
        public void setFolloweesCount(int count) {
            view.setFollowingCount(count);
        }
    }

    public class FollowObserver implements FollowService.Observer {

        @Override
        public void displayError(String message) {
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to follow because of exception: " + ex.getMessage());
        }

        @Override
        public void addFollowees(List<User> followees, boolean hasMorePages) {
            // Empty
        }

        @Override
        public void addFollowers(List<User> followers, boolean hasMorePages) {
            // Empty
        }

        @Override
        public void startActivity(User user) {
            // Empty
        }

        @Override
        public void displayFollowingButton() {
            // Empty
        }

        @Override
        public void displayFollowButton() {
            // Empty
        }

        @Override
        public void updateUserFollowingAndFollowersAndButton(boolean value) {
            view.updateSelectedUserFollowingAndFollowersAndButton(value);
        }

        @Override
        public void enableFollowButton(boolean value) {
            view.enableFollowButton(value);
        }

        @Override
        public void toggleLogoutToast(boolean value) {
            // Empty
        }

        @Override
        public void setFollowersCount(int count) {
            // Empty
        }

        @Override
        public void setFolloweesCount(int count) {
            // Empty
        }
    }

    public class GetStatusObserver implements StatusService.Observer {

        @Override
        public void displayError(String message) {
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to post status because of exception: " + ex.getMessage());
        }

        @Override
        public void startActivity(User user) {
            // Empty
        }

        @Override
        public void addStatuses(List<Status> statuses, boolean hasMorePages) {
            // Empty
        }

        @Override
        public void togglePostingToast(boolean isActive) {
            view.togglePostingToast(isActive);
        }

        @Override
        public void displaySuccess(String message) {
            view.displayMessage(message);
        }
    }
}
