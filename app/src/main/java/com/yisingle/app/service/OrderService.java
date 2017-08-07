package com.yisingle.app.service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


import com.yisingle.app.data.OrderData;
import com.yisingle.app.mvp.IYiSinglePassenger;
import com.yisingle.app.mvp.presenter.YiSinglePassengerPresenterImpl;
import com.yisingle.app.websocket.WebSocketManager;
import com.yisingle.baselibray.base.BaseService;


/**
 * Created by jikun on 17/7/10.
 */

public class OrderService extends BaseService<YiSinglePassengerPresenterImpl> implements IYiSinglePassenger.IYiSinglePassengerView {


    private String TAG = OrderService.class.getSimpleName();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("测试代码", "测试代码--" + TAG + "onCreate");

    }

    @Override
    protected boolean isregisterEventBus() {
        return false;
    }

    @Override
    protected YiSinglePassengerPresenterImpl createPresenter() {
        return new YiSinglePassengerPresenterImpl(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("测试代码", "测试代码--" + TAG + "onStartCommand");
        //mPresenter.repeatFindOrder();
        mPresenter.connectSocket();
        return START_STICKY;
    }


    public static void startService(Context context) {
        Intent intent = new Intent(context, OrderService.class);
        context.startService(intent);
    }

    public static void stopService(Context context) {
        Intent intent = new Intent(context, OrderService.class);
        context.stopService(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        WebSocketManager.getInstance().closeWebSocket();
    }

    @Override
    public void findOrderSuccess(OrderData orderData) {

    }
}
