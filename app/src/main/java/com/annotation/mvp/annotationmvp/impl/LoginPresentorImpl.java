package com.annotation.mvp.annotationmvp.impl;

import com.annotation.mvp.annotationmvp.LoginPresenter;
import com.annotation.mvp.annotationmvp.LoginView;

/**
 * Created by rahul.abrol on 20/02/18.
 */

public class LoginPresentorImpl implements LoginPresenter {

    private LoginView loginView;

    public LoginPresentorImpl(LoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void login(String email, String password) {

    }
}
