package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowerPresenter  extends PagePresenter<User> {
    public GetFollowerPresenter(PagedView<User> view) {
        super(view);
    }

    @Override
    public void callServiceMethod(AuthToken authToken, User user, int pageSize, User lastUser, PagePresenter<User>.PagedObserver observer) {
        new FollowService().loadMoreFollowers(user, pageSize, lastUser, new PagedObserver());
    }

    @Override
    public String getType() {
        return "followers";
    }
}
