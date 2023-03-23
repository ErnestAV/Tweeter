package edu.byu.cs.tweeter.client.model.service.backgroundTasks.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTasks.PagedTask;
import edu.byu.cs.tweeter.client.model.service.observer.BasePageObserver;

public class PageHandler<T> extends BackgroundTaskHandler<BasePageObserver> {
    public PageHandler(BasePageObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(BasePageObserver observer, Bundle data) {
        List<T> items = (List<T>) data.getSerializable(PagedTask.ITEMS_KEY);
        boolean hasMorePages = data.getBoolean(PagedTask.MORE_PAGES_KEY);
        observer.handlePageSuccess(items, hasMorePages);
    }
}
