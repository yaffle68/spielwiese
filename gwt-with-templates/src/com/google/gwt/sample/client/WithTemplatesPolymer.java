package com.google.gwt.sample.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.sample.components.ErrorHandler;
import com.google.gwt.sample.components.LoginComponent;
import com.google.gwt.sample.components.StockDisplayComponent;
import com.google.gwt.sample.components.polymer.PolymerLoginComponent;
import com.google.gwt.sample.components.polymer.PolymerStockDisplayComponent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.vaadin.polymer.paper.widget.PaperButton;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WithTemplatesPolymer implements EntryPoint {
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
        final PolymerStockDisplayComponent stockDisplay = new PolymerStockDisplayComponent(loginInfo);
        RootPanel.get("stockTable").add(stockDisplay);
    }

    private void loadLogin() {
        final PolymerLoginComponent login = new PolymerLoginComponent(loginInfo);
        RootPanel.get("stockTable").add(login);
    }
}
