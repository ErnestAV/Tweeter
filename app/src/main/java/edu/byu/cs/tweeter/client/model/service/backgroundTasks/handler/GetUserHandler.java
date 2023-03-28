package edu.byu.cs.tweeter.client.model.service.backgroundTasks.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTasks.GetUserTask;
import edu.byu.cs.tweeter.model.domain.User;


// TODO: Make a GetUserObserver - Done
public class GetUserHandler extends BackgroundTaskHandler<UserService.GetUserObserver> {
    public GetUserHandler(UserService.GetUserObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(UserService.GetUserObserver observer, Bundle data) {
        User user = (User) data.getSerializable(GetUserTask.USER_KEY);
        observer.handleSuccess(user);
    }
}
