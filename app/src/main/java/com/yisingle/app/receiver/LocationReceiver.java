package com.yisingle.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.map.library.event.LocationEvent;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

/**
 * 定位收取广播器
 */

public class LocationReceiver extends BroadcastReceiver {
    //接受定位数据的广播接收器
    public static String RECEIVER_ACTION = "com.broadcast.location.receiver";

    public final static String Extra_Broadcast_Location_Data = "Broadcast_Data";

    @Override
    public void onReceive(Context context, Intent intent) {
        LocationEvent event = intent.getParcelableExtra(Extra_Broadcast_Location_Data);

       // Logger.d("LocationReceiver---onReceive()---Location_Data");
        if (event != null) {
            EventBus.getDefault().post(event);
        }

    }
}
