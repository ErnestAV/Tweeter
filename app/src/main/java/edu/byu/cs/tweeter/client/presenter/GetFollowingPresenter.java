package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowingPresenter extends PagePresenter<User> {

    public GetFollowingPresenter(PagedView<User> view) {
        super(view);
    }

    @Override
    public void callServiceMethod(AuthToken authToken, User user, int pageSize, User lastUser, PagePresenter<User>.PagedObserver observer) {
        new FollowService().loadMoreFollowees(user, pageSize, lastUser, observer);
    }

    @Override
    public String getType() {
        return "following";
    }

//    private static final int PAGE_SIZE = 10;
//
//    public interface View {
//        void setLoadingFooter(boolean value);
//
//        void displayMessage(String message);
//
//        void addMoreItems(List<User> followees);
//
//        void startActivity(User user);
//    }
//
//    private View view;
//
//    private FollowService followService;
//
//    private User lastFollowee;
//
//    public boolean hasMorePages() {
//        return hasMorePages;
//    }
//
//    public void setHasMorePages(boolean hasMorePages) {
//        this.hasMorePages = hasMorePages;
//    }
//
//    private boolean hasMorePages;
//
//    public boolean isLoading() {
//        return isLoading;
//    }
//
//    public void setLoading(boolean loading) {
//        isLoading = loading;
//    }
//
//    private boolean isLoading = false;
//
//    public GetFollowingPresenter(View view) {
//        this.view = view;
//        followService = new FollowService();
//    }
//
//    public void loadMoreItems(User user) {
//        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
//            setLoading(true);
//            view.setLoadingFooter(isLoading);
//            followService.loadMoreFollowees(user, PAGE_SIZE, lastFollowee, new GetFolloweeObserver());
//        }
//    }
//
//    public void getUserTask(String userAlias) {
//        followService.getUserTask(userAlias, new GetFolloweeObserver());
//        view.displayMessage("Getting user's profile...");
//    }
//
//    public class GetFolloweeObserver implements FollowService.Observer {
//
//        @Override
//        public void displayError(String message) {
//            setLoading(false);
//            view.setLoadingFooter(isLoading);
//            view.displayMessage(message);
//        }
//
//        @Override
//        public void displayException(Exception ex) {
//            setLoading(false);
//            view.setLoadingFooter(isLoading);
//            view.displayMessage("Failed to get following because of exception: " + ex.getMessage());
//        }
//
//        @Override
//        public void addFollowees(List<User> followees, boolean hasMorePages) {
//            setLoading(false);
//            view.setLoadingFooter(isLoading);
//            lastFollowee = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
//            setHasMorePages(hasMorePages);
//            view.addMoreItems(followees);
//        }
//
//        @Override
//        public void addFollowers(List<User> followers, boolean hasMorePages) {
//            // Empty
//        }
//
//        @Override
//        public void startActivity(User user) {
//            view.startActivity(user);
//        }
//
//        @Override
//        public void displayFollowingButton() {
//            // Empty
//        }
//
//        @Override
//        public void displayFollowButton() {
//            // Empty
//        }
//
//        @Override
//        public void updateUserFollowingAndFollowersAndButton(boolean b) {
//            // Empty
//        }
//
//        @Override
//        public void enableFollowButton(boolean value) {
//            // Empty
//        }
//
//        @Override
//        public void toggleLogoutToast(boolean value) {
//            // Empty
//        }
//
//        @Override
//        public void setFollowersCount(int count) {
//            // Empty
//        }
//
//        @Override
//        public void setFolloweesCount(int count) {
//            // Empty
//        }
//    }
}
