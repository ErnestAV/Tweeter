package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowerPresenter {

    private static final int PAGE_SIZE = 10;

    public interface View {
        void setLoadingFooter(boolean value);

        void displayMessage(String message);

        void addMoreItems(List<User> followees);

        void startActivity(User user);
    }


    private View view;
    private FollowService followService;

    private User lastFollower;

    private boolean isLoading = false;

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
    private boolean hasMorePages;


    public GetFollowerPresenter(View view) {
        this.view = view;
        followService = new FollowService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            setLoading(true);
            view.setLoadingFooter(isLoading);
            followService.loadMoreFollowers(user, PAGE_SIZE, lastFollower, new GetFollowerObserver());
        }
    }

    public void getUserTask(String userAlias) {
        followService.getUserTask(userAlias, new GetFollowerObserver());
        view.displayMessage("Getting user's profile...");
    }


    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public class GetFollowerObserver implements FollowService.Observer {

        @Override
        public void displayError(String message) {
            setLoading(false);
            view.setLoadingFooter(isLoading);
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {
            setLoading(false);
            view.setLoadingFooter(isLoading);
            view.displayMessage("Failed to get following because of exception: " + ex.getMessage());
        }

        @Override
        public void addFollowees(List<User> followees, boolean hasMorePages) {
            // Empty
        }

        @Override
        public void addFollowers(List<User> followers, boolean hasMorePages) {
            setLoading(false);
            view.setLoadingFooter(isLoading);
            lastFollower = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addMoreItems(followers);
        }

        @Override
        public void startActivity(User user) {
            view.startActivity(user);
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
            // Empty
        }
    }
}
