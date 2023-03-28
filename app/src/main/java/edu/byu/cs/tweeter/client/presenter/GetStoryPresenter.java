package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class GetStoryPresenter extends PagePresenter<Status> {
    public GetStoryPresenter(PagedView<Status> view) {
        super(view);
    }

    @Override
    public void callServiceMethod(AuthToken authToken, User user, int pageSize, Status lastItem, PagePresenter<Status>.PagedObserver observer) {
        new StatusService().loadMoreStoryItems(user, pageSize, lastItem, new PagedObserver());
    }

    @Override
    public String getType() {
        return "story";
    }
}
