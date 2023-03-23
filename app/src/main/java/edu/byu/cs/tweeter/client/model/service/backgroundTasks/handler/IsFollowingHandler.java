package edu.byu.cs.tweeter.client.model.service.backgroundTasks.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.IsFollowerTask;

public class IsFollowingHandler extends BackgroundTaskHandler<FollowService.IsFollowingObserver> {
    public IsFollowingHandler(FollowService.IsFollowingObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(FollowService.IsFollowingObserver observer, Bundle data) {
        boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        observer.handleSuccess(isFollower);
    }
}
