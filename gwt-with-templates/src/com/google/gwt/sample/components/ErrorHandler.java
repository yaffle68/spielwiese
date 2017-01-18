package com.google.gwt.sample.components;

import com.google.gwt.sample.client.LoginInfo;
import com.google.gwt.sample.client.NotLoggedInException;
import com.google.gwt.user.client.Window;

/**
 * Created by simon.harding on 17.01.2017.
 */
public class ErrorHandler {
    public static void handleError(LoginInfo loginInfo, Throwable error) {
        Window.alert(error.getMessage());
        if (error instanceof NotLoggedInException) {
            Window.Location.replace(loginInfo.getLogoutUrl());
        }
    }

}
