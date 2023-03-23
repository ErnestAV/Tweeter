package edu.byu.cs.tweeter.client.model.service.observer;

import java.util.List;

public interface BasePageObserver<T> extends ServiceObserver {
    void handlePageSuccess(List<T> items, boolean hasMorePages);
}
