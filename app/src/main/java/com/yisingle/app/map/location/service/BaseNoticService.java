package com.yisingle.app.map.location.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by jikun on 17/5/11.
 */


public class BaseNoticService extends Service {
    /**
     * i
     * startForeground的 noti_id
     */
    private int NOTI_ID = 123321;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /**
     * 触发利用notification增加进程优先级
     */
    protected void applyNotiKeepMech() {
        startForeground(NOTI_ID, buildNotification(getBaseContext()));

    }


    protected void unApplyNotiKeepMech() {
        stopForeground(true);
    }


    private static Notification buildNotification(Context context) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentText("service");
        return builder.build();
    }
}
