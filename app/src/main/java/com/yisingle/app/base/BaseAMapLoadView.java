package com.yisingle.app.base;

public abstract class BaseAMapLoadView {
    public abstract void onErrorAmapData();

    public abstract void toastAmapData(String msg);

    public abstract void showLoadingAmapData();

    public abstract void dismissLoadingAmapData();

}