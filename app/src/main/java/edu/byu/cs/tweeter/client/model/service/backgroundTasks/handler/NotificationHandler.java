package edu.byu.cs.tweeter.client.model.service.backgroundTasks.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.observer.NotificationObserver;

public class NotificationHandler extends BackgroundTaskHandler<NotificationObserver> {
    public NotificationHandler(NotificationObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(NotificationObserver observer, Bundle data) {
        observer.handleSuccess();
    }
}
