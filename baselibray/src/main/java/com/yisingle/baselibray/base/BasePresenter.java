package com.yisingle.baselibray.base;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * BasePresenter,子类继承后，在实现对应具体页面的Presenter
 */
public class BasePresenter<V> {


    private CompositeSubscription mCompositeSubscription;
    protected V mView;

    public BasePresenter(V view) {
        this.mView = view;
    }

    public void onDestory() {
        this.mView = null;
        onUnsubscribe();

    }


    /**
     * 所有rx订阅后，需要调用此方法，用于在detachView时取消订阅
     */
    public void addSubscription(Subscription subscribe) {
        if (mCompositeSubscription == null)
            mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(subscribe);
    }

    /**
     * 移除某个subscribe
     */
    public void removeSubscription(Subscription subscribe) {
        if (mCompositeSubscription == null || subscribe == null) return;
        mCompositeSubscription.remove(subscribe);
    }

    /**
     * 取消本页面所有订阅
     */
    protected void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }


}

