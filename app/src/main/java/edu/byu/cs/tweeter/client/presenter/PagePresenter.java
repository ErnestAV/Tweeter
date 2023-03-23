package edu.byu.cs.tweeter.client.presenter;


import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.BasePageObserver;
import edu.byu.cs.tweeter.client.presenter.view.BaseView;
import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagePresenter<T> extends Presenter {
    protected static final int PAGE_SIZE = 10;
    protected boolean hasMorePages;
    protected T lastItem;
    protected boolean isLoading = false;

    public PagePresenter(PagedView<T> view) {
        super(view);
    }

    public boolean isLoading() {
        return isLoading;
    }
    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void getUser(String user) {
        new FollowService().getUserTask(user, new GetUserObserver());

        view.displayMessage("Getting user's profile...");
    }

    public void clickedMention(String clickable) {
        if (clickable.contains("http")) {
            ((PagedView<Status>) view).navigateToURL(clickable);
        } else {
            getUser(clickable);
        }
    }

    public void loadMoreItems(User user) {
        isLoading = true;
        ((PagedView<T> ) view).setLoadingFooter(true);

        callServiceMethod(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem,
                new PagedObserver());

    }

    public abstract void callServiceMethod(AuthToken authToken, User user, int pageSize, T lastItem,
                                           PagedObserver observer);

    private class GetUserObserver implements UserService.GetUserObserver {

        @Override
        public void handleSuccess(User user) {
            ((PagedView<T>) view).navigateToUser(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to get user: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to get user because of exception: " + exception.getMessage());
        }
    }

    public abstract String getType();

    public class PagedObserver implements BasePageObserver<T> {


        public void navigateToUser(User user) {
            ((PagedView<T> ) view).navigateToUser(user);
        }

        @Override
        public void handlePageSuccess(List<T> items, boolean hasMorePages) {
            isLoading = false;
            ((PagedView<T> ) view).setLoadingFooter(false);
            lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
            PagePresenter.this.hasMorePages = hasMorePages;
            ((PagedView<T> ) view).addItems(items);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.displayMessage("Failed to get " + getType() + ": " + message);
        }

        @Override
        public void handleException(Exception ex) {
            isLoading = false;
            view.displayMessage("Failed to get " + getType() + " because of exception: "
                    + ex.getMessage());
        }
    }
}
