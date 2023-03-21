package edu.byu.cs.tweeter.client.model.service.observer;

import java.util.List;

public interface PageObserver<T> extends ServiceObserver {
    void handlePageSuccess(List<T> items, boolean hasMorePages);
}
