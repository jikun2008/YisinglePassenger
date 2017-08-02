package com.yisingle.app.websocket;

import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.google.gson.Gson;

import com.orhanobut.logger.Logger;
import com.yisingle.app.MainApplication;
import com.yisingle.app.base.Constant;
import com.yisingle.app.data.HeartBeatData;
import com.yisingle.app.data.SocketData;
import com.yisingle.app.data.SocketHeadData;
import com.yisingle.app.map.help.AMapLocationHelper;
import com.yisingle.app.utils.ShareprefUtils;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by jikun on 17/7/24.
 */

public class WebSocketManager extends WebSocketListener {


    private static final String TAG = "WebSocketManager----";

    private static final int NORMAL_CLOSURE_STATUS = 1000;

    private static OkHttpClient sClient;
    private static WebSocket sWebSocket;


    private OnWebSocketListener onWebSocketListener;


    // 定义一个私有构造方法
    private WebSocketManager() {
    }

    //定义一个静态私有变量(不初始化，不使用final关键字，使用volatile保证了多线程访问时instance变量的可见性，避免了instance初始化时其他变量属性还没赋值完时，被另外线程调用)
    private static volatile WebSocketManager instance;

    //定义一个共有的静态方法，返回该类型实例
    public static WebSocketManager getInstance() {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (instance == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (WebSocketManager.class) {
                //未初始化，则初始instance变量
                if (instance == null) {
                    instance = new WebSocketManager();
                }
            }
        }
        return instance;
    }


    public void connectWebSocket() {

        if (sClient == null) {
            sClient = new OkHttpClient();
        }
        if (sWebSocket == null) {


            int passengerId = ShareprefUtils.get(Constant.LOGIN_PASSENGER_ID, -1);
            Request request = new Request.Builder().header("passengerId", passengerId + "").url(Constant.getWsBaseUrl() + "yisingle/passenger/websokcet").build();


            sWebSocket = sClient.newWebSocket(request, this);
        }
    }


    private void resetWebSocket() {

        sWebSocket = null;

    }


    public void closeWebSocket() {


        if (sWebSocket != null) {
            sWebSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye!");
            sWebSocket = null;
        }
        if (sClient != null) {
            sClient = null;
        }
        if (instance != null) {
            instance = null;
        }
    }


    public void sendData(String info) {

        if (sWebSocket != null) {
            sWebSocket.send(info);
        } else {
            Logger.d(TAG + "WebSocket sengHeartBeat Failed: ");
        }
    }


    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Logger.d(TAG + "WebSocket onOpen: ");

        if (onWebSocketListener != null) {
            onWebSocketListener.onConnectSuccess();
        }

    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {


        if (onWebSocketListener != null) {
            onWebSocketListener.onGetMsg(text);
        }
        Logger.d(TAG + "onMessage-Receiving:Text= ");
        Logger.json(text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        Logger.d(TAG + "onMessage-Receiving:ByteString= " + bytes.hex());
        if (onWebSocketListener != null) {
            onWebSocketListener.onGetMsg(bytes.hex());
        }
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
        Logger.d(TAG + "Closing: " + code + " " + reason);
        resetWebSocket();

    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        Logger.d(TAG + "Closed: " + code + " " + reason);

        if (sClient != null) {
            if (onWebSocketListener != null) {
                onWebSocketListener.onDisConnect();
            }
        }

    }

    public void sendHeartbeatData() {

        AMapLocation location = AMapLocationHelper.getLastKnownLocation(MainApplication.getContext());


        String latitude = "";
        String longitude = "";
        if (null != location) {
            latitude = location.getLatitude() + "";
            longitude = location.getLongitude() + "";
        }


        if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude)) {
            return;
        }
        SocketData<HeartBeatData> webSocketData = new SocketData<>();
        webSocketData.setCode(0);
        webSocketData.setMsg("心跳数据");
        webSocketData.setType(SocketHeadData.Type.HEART_BEAT);


        HeartBeatData heartData = new HeartBeatData();
        heartData.setCode(0);
        heartData.setLatitude(latitude);
        heartData.setLongitude(longitude);

        int passengerId = ShareprefUtils.get(Constant.LOGIN_PASSENGER_ID, -1);
        heartData.setId(passengerId);
        webSocketData.setResponse(heartData);

        String json = new Gson().toJson(webSocketData);
        if (sWebSocket != null) {
            sWebSocket.send(json);
        } else {
            Logger.d(TAG + "WebSocket sengHeartBeat Failed: ");
        }

    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        t.printStackTrace();
        resetWebSocket();
        if (onWebSocketListener != null) {
            onWebSocketListener.onConnectFailed();
        }
    }


    public void setOnWebSocketListener(OnWebSocketListener onWebSocketListener) {
        this.onWebSocketListener = onWebSocketListener;
    }


    public interface OnWebSocketListener {

        void onConnectSuccess();

        void onConnectFailed();

        void onDisConnect();


        void onGetMsg(String respones);


    }
}
