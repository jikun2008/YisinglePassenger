package com.map.library.rx;


import com.yisingle.baselibray.base.BaseView;

import rx.Subscriber;


/**
 * 订阅时自动显示progressbar的订阅者，必须在主线程订阅
 * 对请求进行统一的错误处理
 *
 * @author yu
 *         Create on 16/7/26.
 */
public abstract class MapSubscriber<T> extends Subscriber<T> {

    private BaseView mBaseView;

    private int type = 0;

    private boolean isShowLoading = true;


    /**
     * 传null不显示loading也不显示toast
     */
    public MapSubscriber(BaseView mBaseView, int type, boolean isShowLoading) {
        this.mBaseView = mBaseView;
        this.type = type;
        this.isShowLoading = isShowLoading;

    }

    /**
     * 传null不显示loading也不显示toast
     */
    public MapSubscriber(BaseView mBaseView, int type) {
        this.mBaseView = mBaseView;
        this.type = type;
        this.isShowLoading = true;

    }


    @Override
    public void onStart() {
        if (isShowLoading) {
            if (mBaseView != null) mBaseView.showLoading(type);
        }

    }

    @Override
    public void onCompleted() {
        if (isShowLoading) {
            if (mBaseView != null) mBaseView.dismissLoading(type);
        }
    }


    /**
     * 只要链式调用中抛出了异常都会走这个回调
     */
    public void onError(Throwable e) {
        e.printStackTrace();
        if (mBaseView != null) {
            if (isShowLoading) {
                mBaseView.dismissLoading(type);
            }
            mBaseView.onError(type);
        }

    }
}
