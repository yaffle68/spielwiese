package com.google.gwt.sample.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.sample.components.ErrorHandler;
import com.google.gwt.sample.components.LoginComponent;
import com.google.gwt.sample.components.StockDisplayComponent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WithTemplates implements EntryPoint {
    /**
     * The message displayed to the user when the server cannot be reached or
     * returns an error.
     */
    private static final String SERVER_ERROR = "An error occurred while "
            + "attempting to contact the server. Please check your network "
            + "connection and try again.";

    private LoginServiceAsync loginService = GWT.create(LoginService.class);
    private LoginInfo loginInfo;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.handleError(loginInfo, caught);
            }

            @Override
            public void onSuccess(LoginInfo result) {
                loginInfo = result;
                if (loginInfo.isLoggedIn()) {
                    loadStockWatcher();
                } else {
                    loadLogin();
                }
            }
        });
    }

     /**
     * This is the entry point method.
     */
    public void loadStockWatcher() {
        final StockDisplayComponent stockDisplay = new StockDisplayComponent(loginInfo);
        RootPanel.get("stockTable").add(stockDisplay);
    }

    private void loadLogin() {
        final LoginComponent login = new LoginComponent(loginInfo);
        RootPanel.get("stockTable").add(login);
    }

}
