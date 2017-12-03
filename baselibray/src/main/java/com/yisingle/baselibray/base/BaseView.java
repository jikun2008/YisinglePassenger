package com.yisingle.baselibray.base;

public interface BaseView {
    void onError(int type);

    void toast(String msg);

    void showLoading(int type);

    void dismissLoading(int type);

}