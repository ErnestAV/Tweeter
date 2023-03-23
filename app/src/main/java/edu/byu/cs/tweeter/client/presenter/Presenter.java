package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.presenter.view.BaseView;

public class Presenter {
    protected BaseView view;
    public Presenter(BaseView view) {
        this.view = view;
    }
}
