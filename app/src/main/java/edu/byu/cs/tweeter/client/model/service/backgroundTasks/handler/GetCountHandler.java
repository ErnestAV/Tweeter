package edu.byu.cs.tweeter.client.model.service.backgroundTasks.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTasks.GetCountTask;
import edu.byu.cs.tweeter.client.model.service.observer.CountObserver;

public class GetCountHandler extends BackgroundTaskHandler<CountObserver> {

    public GetCountHandler(CountObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(CountObserver observer, Bundle data) {
        int count = data.getInt(GetCountTask.COUNT_KEY);
        observer.handleSuccess(count);
    }
}
