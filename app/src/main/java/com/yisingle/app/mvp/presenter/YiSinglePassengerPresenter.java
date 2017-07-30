package com.yisingle.app.mvp.presenter;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yisingle.app.base.BasePresenter;
import com.yisingle.app.data.SocketData;
import com.yisingle.app.data.SocketHeadData;
import com.yisingle.app.mvp.IYiSinglePassenger;
import com.yisingle.app.utils.JsonUtils;
import com.yisingle.app.websocket.WebSocketManager;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;

/**
 * Created by jikun on 17/7/24.
 */

public class YiSinglePassengerPresenter extends BasePresenter<IYiSinglePassenger.IYiSinglePassengerView> implements IYiSinglePassenger.IYiSinglePassengerPresenter {

    private Subscription subscription;

    private Gson gson = new Gson();


    private String TAG = YiSinglePassengerPresenter.class.getSimpleName();

    public YiSinglePassengerPresenter(IYiSinglePassenger.IYiSinglePassengerView view) {
        super(view);
        WebSocketManager.getInstance().setOnWebSocketListener(new WebSocketManager.OnWebSocketListener() {
            @Override
            public void onConnectSuccess() {
                sengHeadBeatDataDelay(2);
            }

            @Override
            public void onConnectFailed() {
                connectWebSocketDelay(2);

            }

            @Override
            public void onDisConnect() {
                connectWebSocketDelay(2);
            }


            @Override
            public void onGetMsg(String respones) {

                try {


                    if (JsonUtils.isGoodJson(respones)) {

                        SocketHeadData headData = gson.fromJson(respones, SocketHeadData.class);
                        switch (headData.getType()) {
                            case SocketData.Type.HEART_BEAT:
                                sengHeadBeatDataDelay(5);
                                break;
                            case SocketData.Type.ORDER_NEW:
//                                SocketData<OrderEntity> data = gson.fromJson(respones, new TypeToken<SocketData<OrderEntity>>() {
//                                }.getType());
//                                mView.findOrderSuccess(data.getResponse());
//                                WebSocketManager.getInstance().sendData(respones);//收到消息后把消息返回的服务器告诉服务器我接受到了订单
                                break;
                            case SocketData.Type.PRICIE_ORDER:
//                                SocketData<OrderEntity> price = gson.fromJson(respones, new TypeToken<SocketData<OrderEntity>>() {
//                                }.getType());
//                                //发送价格订单到OrderService
//                                EventBus.getDefault().post(price.getResponse());
                                break;
                        }
                    } else {

                        Logger.e("socket收到数据respones=" + respones + "不合法");

                    }
                } catch (Exception e) {
                    Logger.e("socket收到数据respones=" + respones + "不合法Exception=" + e.toString());
                }
                ;
            }
        });
    }


    private void sengHeadBeatDataDelay(int time) {
        Observable.just("").delay(time, TimeUnit.SECONDS).subscribe(
                s -> {
                    WebSocketManager.getInstance().sendHeartbeatData();
                }
        );
    }

    @Override
    public void connectSocket() {

        WebSocketManager.getInstance().connectWebSocket();
    }

    private void connectWebSocketDelay(int time) {
        Observable.just("").delay(time, TimeUnit.SECONDS).subscribe(
                s -> {
                    WebSocketManager.getInstance().connectWebSocket();
                }
        );
    }


    @Override
    public void onDestory() {
        super.onDestory();
        if (null != subscription) {
            subscription.unsubscribe();
        }
        WebSocketManager.getInstance().closeWebSocket();
    }
}
