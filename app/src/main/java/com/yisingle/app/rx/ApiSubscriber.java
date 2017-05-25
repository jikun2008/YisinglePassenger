package com.yisingle.app.rx;


import com.yisingle.app.base.BaseView;
import com.yisingle.app.http.ErrorHandler;

import rx.Subscriber;

/**
 * 订阅时自动显示progressbar的订阅者，必须在主线程订阅
 * 对请求进行统一的错误处理
 *
 * @author yu
 *         Create on 16/7/26.
 */
public abstract class ApiSubscriber<T> extends Subscriber<T> {

    private BaseView mBaseView;
    private boolean showToast;

    private boolean isshowLoading = true;

    /**
     * 传null不显示loading也不显示toast
     */
    public ApiSubscriber(BaseView mBaseView) {
        this.mBaseView = mBaseView;
        this.showToast = mBaseView != null;
    }

    /**
     * @param mBaseView baseview
     * @param showToast 是否显示toast
     */
    public ApiSubscriber(BaseView mBaseView, boolean showToast) {
        this.mBaseView = mBaseView;
        this.showToast = showToast;
    }


    /**
     * @param mBaseView baseview
     * @param showToast 是否显示toast
     */
    public ApiSubscriber(BaseView mBaseView, boolean showToast, boolean isshowLoading) {
        this.mBaseView = mBaseView;
        this.showToast = showToast;
        this.isshowLoading = isshowLoading;
    }

    @Override
    public void onStart() {
        if (isshowLoading && mBaseView != null) {
            mBaseView.showLoading();
        }

    }

    @Override
    public void onCompleted() {
        if (isshowLoading && mBaseView != null) {
            mBaseView.dismissLoading();
        }

    }

    /**
     * 只要链式调用中抛出了异常都会走这个回调
     */
    public void onError(Throwable e) {
        e.printStackTrace();

        if (isshowLoading && mBaseView != null) {
            mBaseView.dismissLoading();
            mBaseView.onError();
        }


        ErrorHandler.handleException(e, showToast);
    }
}