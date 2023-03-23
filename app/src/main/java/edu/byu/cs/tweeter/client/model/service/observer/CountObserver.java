package edu.byu.cs.tweeter.client.model.service.observer;

public interface CountObserver extends ServiceObserver {
    public void handleSuccess(int count);
}
