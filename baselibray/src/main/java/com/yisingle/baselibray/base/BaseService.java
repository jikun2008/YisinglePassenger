package com.yisingle.baselibray.base;

import android.app.Service;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by jikun on 17/7/24.
 */

public abstract class BaseService<P extends BasePresenter> extends Service implements BaseView {

    protected P mPresenter;

    @Override
    public void onCreate() {
        super.onCreate();
        mPresenter = createPresenter();
        if (isregisterEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    protected abstract boolean isregisterEventBus();


    protected abstract P createPresenter();


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (null != mPresenter) {
            mPresenter.onDestory();
        }
    }

    @Override
    public void onError(int type) {

    }

    @Override
    public void toast(String msg) {

    }

    @Override
    public void showLoading(int type) {

    }

    @Override
    public void dismissLoading(int type) {

    }
}
