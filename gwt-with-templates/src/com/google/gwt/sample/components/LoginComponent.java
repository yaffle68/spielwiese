package com.google.gwt.sample.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.sample.client.LoginInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

/**
 * Created by simon.harding on 16.01.2017.
 */
public class LoginComponent extends Composite {

    interface MyUiBinder extends UiBinder<Widget, LoginComponent> {}
    private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    VerticalPanel loginPanel;

    @UiField
    Anchor loginLink;

    @UiField
    Label loginLabel;

    public LoginComponent(final LoginInfo loginInfo) {
        // Assemble login panel.
        initWidget(uiBinder.createAndBindUi(this));
        loginLabel.setText("This is the login label...");
        loginLink.setText("Sign in...");
        loginLink.setHref(loginInfo.getLoginUrl());
//        loginPanel.add(loginLabel);
//        loginPanel.add(loginLink);
     }
}
