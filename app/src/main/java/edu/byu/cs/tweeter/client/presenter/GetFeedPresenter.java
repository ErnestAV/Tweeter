package edu.byu.cs.tweeter.client.presenter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.GetFeedTask;
import edu.byu.cs.tweeter.client.view.main.feed.FeedFragment;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFeedPresenter {

    private static final int PAGE_SIZE = 10;

    public interface View {

        void setLoadingFooter(boolean isLoading);

        void displayMessage(String message);

        void startActivity(User user);

        void addMoreItems(List<Status> statuses);
    }

    private View view;

    private StatusService statusService;

    private boolean isLoading = false;

    public boolean isLoading() { return isLoading; }

    public void setLoading(boolean loading) { isLoading = loading; }

    private boolean hasMorePages;


    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    private Status lastStatus;

    public GetFeedPresenter(View view) {
        this.view = view;
        statusService = new StatusService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {
            setLoading(true);
            view.setLoadingFooter(isLoading);
            statusService.loadMoreFeedItems(user, PAGE_SIZE, lastStatus, new GetFeedObserver());
        }
    }

    public void getUserTask(String userAlias) {
        statusService.getUserTask(userAlias, new GetFeedObserver());
        view.displayMessage("Getting user's profile...");
    }

    public class GetFeedObserver implements StatusService.Observer {

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
            view.displayMessage("Failed to get feed because of exception: " + ex.getMessage());
        }

        @Override
        public void startActivity(User user) {
            view.startActivity(user);
        }

        @Override
        public void addStatuses(List<Status> statuses, boolean hasMorePages) {
            setLoading(false);
            view.setLoadingFooter(isLoading);
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addMoreItems(statuses);
        }

        @Override
        public void togglePostingToast(boolean isActive) {
            // Empty
        }

        @Override
        public void displaySuccess(String message) {
            // Empty
        }
    }
}
