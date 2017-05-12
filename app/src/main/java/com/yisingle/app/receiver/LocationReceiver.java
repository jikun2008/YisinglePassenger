package com.yisingle.app.receiver;

/**
 * Created by jikun on 17/5/12.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yisingle.app.event.LocationEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * 定位收取广播器
 */
public class LocationReceiver extends BroadcastReceiver {

    public static String LOCATION = "Loction_event";

    @Override
    public void onReceive(Context context, Intent intent) {
        LocationEvent event = intent.getParcelableExtra(LocationReceiver.LOCATION);

        if (event != null) {
            EventBus.getDefault().post(event);
        }

    }
}
