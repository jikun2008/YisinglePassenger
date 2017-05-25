package com.yisingle.app.base;

public interface BaseView {
    void onError();

    void toast(String msg);

    void showLoading();

    void dismissLoading();

}