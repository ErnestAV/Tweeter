package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.handler.GetCountHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.handler.IsFollowingHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.handler.NotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.handler.PageHandler;
import edu.byu.cs.tweeter.client.model.service.observer.BasePageObserver;
import edu.byu.cs.tweeter.client.model.service.observer.NotificationObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.presenter.GetMainPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends Service {

    /** Observer Interfaces **/
    public interface FollowObserver extends NotificationObserver {}
    public interface UnfollowObserver extends NotificationObserver {}
    public interface IsFollowingObserver extends ServiceObserver {
        void handleSuccess(boolean value);
    }
    public interface FollowingObserver extends BasePageObserver<User> {
        void navigateToUser(User user);
    }

//    public interface Observer {
//
//        void displayError(String message);
//
//        void displayException(Exception ex);
//
//        void addFollowees(List<User> followees, boolean hasMorePages);
//
//        void addFollowers(List<User> followers, boolean hasMorePages);
//
//        void startActivity(User user);
//
//        void displayFollowingButton();
//
//        void displayFollowButton();
//
//        void updateUserFollowingAndFollowersAndButton(boolean value);
//
//        void enableFollowButton(boolean value);
//
//        void toggleLogoutToast(boolean isActive);
//
//        void setFollowersCount(int count);
//
//        void setFolloweesCount(int count);
//    }

    /** Tasks */

    public void getUserTask(String userAlias, UserService.GetUserObserver getUserObserver) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new GetUserHandler(getUserObserver));
        executeTask(getUserTask);
    }

    public void getIsFollowerTask(User theUser, User toCheckUser, IsFollowingObserver isFollowingObserver) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                theUser, toCheckUser, new IsFollowingHandler(isFollowingObserver));
        executeTask(isFollowerTask);
    }

    public void unfollowTask(User selectedUser, UnfollowObserver unfollowObserver) {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new NotificationHandler(unfollowObserver));
        executeTask(unfollowTask);
    }

    public void followTask(User selectedUser, FollowObserver followObserver) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new NotificationHandler(followObserver));
        executeTask(followTask);
    }

    // TODO: FOLLOWERS AND FOLLOWING COUNT INTO ONE TASK WITH A DOUBLE THREAD EXECUTOR
//    public void followersCountTask(User selectedUser, ExecutorService executor, Observer observer) {
//        // Get count of most recently selected user's followers.
//        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
//                selectedUser, new GetFollowersCountHandler(observer));
//        executor.execute(followersCountTask);
//    }
//
//    public void followingCountTask(User selectedUser, ExecutorService executor, Observer observer) {
//        // Get count of most recently selected user's followees (who they are following)
//        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
//                selectedUser, new GetFollowingCountHandler(observer));
//        executor.execute(followingCountTask);
//    }
    public void updateFollowersAndFollowingTask(User selectedUser, GetMainPresenter.FollowerCountObserver followerCountObserver, GetMainPresenter.FollowingCountObserver followingCountObserver) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetCountHandler(followerCountObserver));
        executor.execute(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetCountHandler(followingCountObserver));
        executor.execute(followingCountTask);

    }

    // COMBINED ABOVE

    /** Loaders */
    public void loadMoreFollowees(User user, int pageSize, User lastFollowUser, BasePageObserver<User> followingObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollowUser, new PageHandler<User>(followingObserver));
        executeTask(getFollowingTask);
    }

    public void loadMoreFollowers(User user, int pageSize, User lastFollowUser, BasePageObserver<User> followerObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollowUser, new PageHandler<User>(followerObserver));
        executeTask(getFollowersTask);
    }

//    // TODO: GET RID OF ALL THESE HANDLERS
//    /**
//     * Message handler (i.e., observer) for GetFollowingTask.
//     */
//    private class GetFollowingHandler extends Handler {
//
//        private Observer observer;
//        public GetFollowingHandler(Observer observer) {
//            super(Looper.getMainLooper());
//            this.observer = observer;
//        }
//
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(GetFollowingTask.SUCCESS_KEY);
//            if (success) {
//                List<User> followees = (List<User>) msg.getData().getSerializable(GetFollowingTask.ITEMS_KEY);
//                boolean hasMorePages = msg.getData().getBoolean(GetFollowingTask.MORE_PAGES_KEY);
//                observer.addFollowees(followees, hasMorePages);
//            } else if (msg.getData().containsKey(GetFollowingTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(GetFollowingTask.MESSAGE_KEY);
//                observer.displayError("Failed to get following: " + message);
//            } else if (msg.getData().containsKey(GetFollowingTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingTask.EXCEPTION_KEY);
//                observer.displayException(ex);
//            }
//        }
//    }
//
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
//
//    /**
//     * Message handler (i.e., observer) for GetFollowersTask.
//     */
//    private class GetFollowersHandler extends Handler {
//        private Observer observer;
//
//        public GetFollowersHandler(Observer observer) {
//            super(Looper.getMainLooper());
//            this.observer = observer;
//        }
//
//        @Override
//        public void handleMessage(@NonNull Message msg) { // TODO: Get out of here
//            boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
//            if (success) {
//                List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.ITEMS_KEY);
//                boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);
//                observer.addFollowers(followers, hasMorePages);
//            } else if (msg.getData().containsKey(GetFollowersTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(GetFollowersTask.MESSAGE_KEY);
//                observer.displayError("Failed to get followers: " + message);
//            } else if (msg.getData().containsKey(GetFollowersTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersTask.EXCEPTION_KEY);
//                observer.displayException(ex);
//            }
//        }
//    }
//
//    // IsFollowerHandler
//
//    private class IsFollowerHandler extends Handler {
//
//        private Observer observer;
//
//        public IsFollowerHandler(Observer observer) {
//            super(Looper.getMainLooper());
//            this.observer = observer;
//        }
//
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(IsFollowerTask.SUCCESS_KEY);
//            if (success) {
//                boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
//
//                // If logged in user is a follower of the selected user, display the follow button as "following"
//                if (isFollower) {
//                    observer.displayFollowingButton();
//                } else {
//                    observer.displayFollowButton();
//                }
//            } else if (msg.getData().containsKey(IsFollowerTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(IsFollowerTask.MESSAGE_KEY);
//                observer.displayError("Failed to determine following relationship: " + message);
//            } else if (msg.getData().containsKey(IsFollowerTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(IsFollowerTask.EXCEPTION_KEY);
//                observer.displayException(ex);
//            }
//        }
//    }
//
//    // UnfollowHandler
//
//    private class UnfollowHandler extends Handler {
//
//        Observer observer;
//
//        public UnfollowHandler(Observer observer) {
//            super(Looper.getMainLooper());
//            this.observer = observer;
//        }
//
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(UnfollowTask.SUCCESS_KEY);
//            if (success) {
//                observer.updateUserFollowingAndFollowersAndButton(true);
//            } else if (msg.getData().containsKey(UnfollowTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(UnfollowTask.MESSAGE_KEY);
//                observer.displayError("Failed to unfollow: " + message);
//            } else if (msg.getData().containsKey(UnfollowTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(UnfollowTask.EXCEPTION_KEY);
//                observer.displayException(ex);
//            }
//
//            observer.enableFollowButton(true);
//        }
//    }
//
//    // GetFollowersCountHandler
//
//    private class GetFollowersCountHandler extends Handler {
//
//        Observer observer;
//        public GetFollowersCountHandler(Observer observer) {
//            super(Looper.getMainLooper());
//            this.observer = observer;
//        }
//
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(GetFollowersCountTask.SUCCESS_KEY);
//            if (success) {
//                int count = msg.getData().getInt(GetFollowersCountTask.COUNT_KEY);
//                observer.setFollowersCount(count);
//            } else if (msg.getData().containsKey(GetFollowersCountTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(GetFollowersCountTask.MESSAGE_KEY);
//                observer.displayError("Failed to get followers count: " + message);
//            } else if (msg.getData().containsKey(GetFollowersCountTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersCountTask.EXCEPTION_KEY);
//                observer.displayException(ex);
//            }
//        }
//    }
//
//    // GetFollowingCountHandler
//
//    private class GetFollowingCountHandler extends Handler {
//
//        Observer observer;
//
//        public GetFollowingCountHandler(Observer observer) {
//            super(Looper.getMainLooper());
//            this.observer = observer;
//        }
//
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(GetFollowingCountTask.SUCCESS_KEY);
//            if (success) {
//                int count = msg.getData().getInt(GetFollowingCountTask.COUNT_KEY);
//                observer.setFolloweesCount(count);
//            } else if (msg.getData().containsKey(GetFollowingCountTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(GetFollowingCountTask.MESSAGE_KEY);
//                observer.displayError("Failed to get following count: " + message);
//            } else if (msg.getData().containsKey(GetFollowingCountTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingCountTask.EXCEPTION_KEY);
//                observer.displayException(ex);
//            }
//        }
//    }
//
//    // FollowHandler
//
//    private class FollowHandler extends Handler {
//
//        Observer observer;
//        public FollowHandler(Observer observer) {
//            super(Looper.getMainLooper());
//            this.observer = observer;
//        }
//
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
//            if (success) {
//                observer.updateUserFollowingAndFollowersAndButton(false);
//            } else if (msg.getData().containsKey(FollowTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(FollowTask.MESSAGE_KEY);
//                observer.displayError("Failed to follow: " + message);
//            } else if (msg.getData().containsKey(FollowTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(FollowTask.EXCEPTION_KEY);
//                observer.displayException(ex);
//            }
//
//            observer.enableFollowButton(true);
//        }
//    }
}
