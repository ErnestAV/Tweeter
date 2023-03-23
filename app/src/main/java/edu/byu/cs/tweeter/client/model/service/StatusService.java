package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.handler.NotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.handler.PageHandler;
import edu.byu.cs.tweeter.client.model.service.observer.BasePageObserver;
import edu.byu.cs.tweeter.client.model.service.observer.NotificationObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService extends Service {

    public interface PostStatusObserver extends NotificationObserver {}

//    public interface Observer {
//        void displayError(String message);
//
//        void displayException(Exception ex);
//
//        void startActivity(User user);
//
//        void addStatuses(List<Status> statuses, boolean hasMorePages);
//
//        void togglePostingToast(boolean isActive);
//
//        void displaySuccess(String message);
//    }

//    public void getUserTask(String userAlias, Observer observer) {
//        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
//                userAlias, new GetUserHandler(observer));
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(getUserTask);
//    }

    public void getStatusTask(Status newStatus, PostStatusObserver observer) {
        PostStatusTask statusTask = new PostStatusTask(Cache.getInstance().getCurrUserAuthToken(),
                newStatus, new NotificationHandler(observer));
        executeTask(statusTask);
    }

    public void loadMoreFeedItems(User user, int pageSize, Status lastStatus, BasePageObserver<Status> observer) {
        GetFeedTask getFeedTask = new GetFeedTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastStatus, new PageHandler<Status>(observer));
        executeTask(getFeedTask);
    }

    public void loadMoreStoryItems(User user, int pageSize, Status lastStatus, BasePageObserver<Status> observer) {
        GetStoryTask getStoryTask = new GetStoryTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastStatus, new PageHandler<Status>(observer));
        executeTask(getStoryTask);
    }

// TODO: GET RID OF THE HANDLERS
//    /**
//     * Message handler (i.e., observer) for GetUserTask.
//     */
//    private class GetUserHandler extends Handler {
//
//        private Observer observer;
//
//        public GetUserHandler(Observer observer) {
//            super(Looper.getMainLooper());
//            this.observer = observer;
//        }
//
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
//            if (success) {
//                User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
//                observer.startActivity(user);
//            } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
//                observer.displayError("Failed to get user's profile: " + message);
//            } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
//                observer.displayException(ex);
//            }
//        }
//    }
//
//    /**
//     * Message handler (i.e., observer) for GetFeedTask.
//     */
//    private class GetFeedHandler extends Handler {
//
//        private Observer observer;
//        public GetFeedHandler(Observer observer) {
//            super(Looper.getMainLooper());
//            this.observer = observer;
//        }
//
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(GetFeedTask.SUCCESS_KEY);
//            if (success) {
//                List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetFeedTask.ITEMS_KEY);
//                boolean hasMorePages = msg.getData().getBoolean(GetFeedTask.MORE_PAGES_KEY);
//                observer.addStatuses(statuses, hasMorePages);
//            } else if (msg.getData().containsKey(GetFeedTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(GetFeedTask.MESSAGE_KEY);
//                observer.displayError("Failed to get feed: " + message);
//            } else if (msg.getData().containsKey(GetFeedTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(GetFeedTask.EXCEPTION_KEY);
//                observer.displayException(ex);
//            }
//        }
//    }
//
//    /**
//     * Message handler (i.e., observer) for GetStoryTask.
//     */
//    private class GetStoryHandler extends Handler {
//
//        private Observer observer;
//
//        public GetStoryHandler(Observer observer) {
//            super(Looper.getMainLooper());
//            this.observer = observer;
//        }
//
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(GetStoryTask.SUCCESS_KEY);
//            if (success) {
//                List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetStoryTask.ITEMS_KEY);
//                boolean hasMorePages = msg.getData().getBoolean(GetStoryTask.MORE_PAGES_KEY);
//                observer.addStatuses(statuses, hasMorePages);
//            } else if (msg.getData().containsKey(GetStoryTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(GetStoryTask.MESSAGE_KEY);
//                observer.displayError("Failed to get story: " + message);
//            } else if (msg.getData().containsKey(GetStoryTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(GetStoryTask.EXCEPTION_KEY);
//                observer.displayException(ex);
//            }
//        }
//    }
//
//    // PostStatusHandler
//
//    private class PostStatusHandler extends Handler {
//
//        Observer observer;
//
//        public PostStatusHandler(Observer observer) {
//            super(Looper.getMainLooper());
//            this.observer = observer;
//        }
//
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(PostStatusTask.SUCCESS_KEY);
//            if (success) {
//                observer.togglePostingToast(false);
//                observer.displaySuccess("Successfully Posted!");
//            } else if (msg.getData().containsKey(PostStatusTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(PostStatusTask.MESSAGE_KEY);
//                observer.displayError("Failed to post status: " + message);
//            } else if (msg.getData().containsKey(PostStatusTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(PostStatusTask.EXCEPTION_KEY);
//                observer.displayException(ex);
//            }
//        }
//    }
}
