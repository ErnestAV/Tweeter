package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.handler.NotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.handler.PageHandler;
import edu.byu.cs.tweeter.client.model.service.observer.BasePageObserver;
import edu.byu.cs.tweeter.client.model.service.observer.NotificationObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService extends Service {

    public interface PostStatusObserver extends NotificationObserver {}

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
}
