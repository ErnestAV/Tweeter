package edu.byu.cs.tweeter.client.model.service;

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

    // TODO: FOLLOWERS AND FOLLOWING COUNT INTO ONE TASK WITH A DOUBLE THREAD EXECUTOR -- Done
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
}
