package edu.byu.cs.tweeter.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import java.text.ParseException;
import org.junit.jupiter.api.Assertions;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.GetMainPresenter;
import edu.byu.cs.tweeter.model.domain.Status;

public class StatusPostTests {
    StatusService mockStatusService;
    Status mockStatus;
    GetMainPresenter.View mockView;
    GetMainPresenter spyPresenter;

    @BeforeEach
    public void setup() {
        mockStatusService = Mockito.mock(StatusService.class);
        mockStatus = Mockito.mock(Status.class);
        mockView = Mockito.mock(GetMainPresenter.View.class);
        spyPresenter = Mockito.spy(new GetMainPresenter(mockView));

        Mockito.when(spyPresenter.getStatusService()).thenReturn(mockStatusService);
    }

    @Test
    public void TestSuccessfulPost() throws ParseException {
        Answer<Void> result = invocation -> {
            GetMainPresenter.PostStatusObserver postStatusObserver = invocation.getArgument(1, GetMainPresenter.PostStatusObserver.class);
            postStatusObserver.handleSuccess();
            return null;
        };

        // Making sure parameters passed in are the right ones
        Mockito.doAnswer(result).when(mockStatusService).getStatusTask(Mockito.isA(Status.class), Mockito.isA(GetMainPresenter.PostStatusObserver.class));
        spyPresenter.getStatusPostedTask("test post");

        Mockito.verify(mockView).displayMessage("Posting Status...");
        Mockito.verify(mockView).clearMessage();
        Mockito.verify(mockView).displayMessage("Successfully Posted!");
    }

    @Test
    public void TestFailurePost() throws ParseException {
        Answer<Void> result = invocation -> {
            GetMainPresenter.PostStatusObserver observer = invocation.getArgument(1, GetMainPresenter.PostStatusObserver.class);
            observer.handleFailure("Failed to post status: <ERROR MESSAGE>");
            return null;
        };

        // Making sure parameters passed in are the right ones
        Mockito.doAnswer(result).when(mockStatusService).getStatusTask(Mockito.isA(Status.class), Mockito.isA(GetMainPresenter.PostStatusObserver.class));
        spyPresenter.getStatusPostedTask("test post");

        Mockito.verify(mockView).displayMessage("Posting Status...");
        Mockito.verify(mockView).displayMessage("Failed to post status: Failed to post status: <ERROR MESSAGE>");
    }

    @Test
    public void TestFailureDueToExceptionPost() throws ParseException {
        Answer<Void> result = invocation -> {
            GetMainPresenter.PostStatusObserver observer = invocation.getArgument(1, GetMainPresenter.PostStatusObserver.class);
            observer.handleException(new Exception("Failed to post status because of exception: <EXCEPTION MESSAGE>"));
            return null;
        };

        // Making sure parameters passed in are the right ones
        Mockito.doAnswer(result).when(mockStatusService).getStatusTask(Mockito.isA(Status.class), Mockito.isA(GetMainPresenter.PostStatusObserver.class));
        spyPresenter.getStatusPostedTask("test post");

        Mockito.verify(mockView).displayMessage("Posting Status...");
        Mockito.verify(mockView).displayMessage("Failed to post status due to exception: java.lang.Exception: Failed to post status because of exception: <EXCEPTION MESSAGE>");
    }
}
