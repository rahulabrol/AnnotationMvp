package com.annotation.mvp.annotationmvp;

import com.annotation.mvp.annotationmvp.core.MVP;
import com.annotation.mvp.annotationmvp.core.Presenter;
import com.annotation.mvp.annotationmvp.core.View;

/**
 * Created by rahul.abrol on 14/02/18.
 */
@MVP
public interface Login {
    @Presenter
    void login(String email, String password);

    @View
    void showEmailError(String error);

    @View
    void showPasswordError(String error);

    @View
    void onSuccessLogin();
}
