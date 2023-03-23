package edu.byu.cs.tweeter.client.model.service.backgroundTasks.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTasks.AuthenticateTask;
import edu.byu.cs.tweeter.client.model.service.observer.LoginRegisterObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginRegisterHandler extends BackgroundTaskHandler<LoginRegisterObserver> {

    public LoginRegisterHandler(LoginRegisterObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(LoginRegisterObserver observer, Bundle data) {
        User user = (User) data.getSerializable(AuthenticateTask.USER_KEY);
        AuthToken authToken = (AuthToken) data.getSerializable(AuthenticateTask.AUTH_TOKEN_KEY);

        observer.handleSuccess(user, authToken);
    }
}
