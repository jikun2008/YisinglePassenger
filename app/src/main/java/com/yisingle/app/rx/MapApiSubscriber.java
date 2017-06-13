package com.yisingle.app.rx;

import com.yisingle.app.base.BaseAMapLoadView;

import rx.Subscriber;

/**
 * Created by jikun on 17/6/13.
 */

public abstract class MapApiSubscriber<T> extends Subscriber<T> {

    private BaseAMapLoadView mapLoadView;

    private boolean isshowLoading = true;

    /**
     * 传null不显示loading也不显示toast
     */
    public MapApiSubscriber(BaseAMapLoadView baseAMapLoadView) {
        this.mapLoadView = baseAMapLoadView;
    }


    @Override
    public void onStart() {
        if (isshowLoading && mapLoadView != null) {
            mapLoadView.showLoadingAmapData();
        }

    }

    @Override
    public void onCompleted() {
        if (isshowLoading && mapLoadView != null) {
            mapLoadView.dismissLoadingAmapData();
        }

    }

    /**
     * 只要链式调用中抛出了异常都会走这个回调
     */
    public void onError(Throwable e) {
        e.printStackTrace();

        if (isshowLoading && mapLoadView != null) {
            mapLoadView.dismissLoadingAmapData();
            mapLoadView.onErrorAmapData();
        }


    }


}
