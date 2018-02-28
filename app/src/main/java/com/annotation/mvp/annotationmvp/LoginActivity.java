package com.annotation.mvp.annotationmvp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.annotation.mvp.annotationmvp.impl.LoginPresentorImpl;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginPresentorImpl(this);
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showEmailError(String error) {

    }

    @Override
    public void showPasswordError(String error) {

    }

    @Override
    public void onSuccessLogin() {

    }
}
