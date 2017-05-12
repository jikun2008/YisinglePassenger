package com.yisingle.app.map.location.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.yisingle.bind.IGuardAidlInterface;

import java.util.List;


/**
 * Created by jikun on 17/5/11.
 */

public class GuardService extends BaseNoticService {
    //需要守护的定位服务的名称
    private final String locationServiceName = "com.yisingle.app.map.location.service.LocationService";

    private ServiceConnection mInnerConnection;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 开始绑定LocationService开始守护LocationService
         * 当mInnerConnection收到LocationService断开了连接那么重新打开LocationService
         */
        Log.e("1", "测试代码：GuardService-------onCreate");
        startBind();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("1", "测试代码：GuardService-------onStartCommand");
        return START_STICKY;
    }


    private void startBind() {

        mInnerConnection = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.e("1", "测试代码：GuardService-------onServiceDisconnected");
                Intent intent = new Intent();
                intent.setAction(locationServiceName);
                startService(getExplicitIntent(getApplicationContext(), intent));
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                IGuardAidlInterface l = IGuardAidlInterface.Stub.asInterface(service);
                try {
                    l.connect();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };

        Intent intent = new Intent();
        intent.setAction(locationServiceName);
        bindService(getExplicitIntent(getApplicationContext(), intent), mInnerConnection, Service.BIND_AUTO_CREATE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("1", "测试代码：GuardService-------onDestroy");
        if (mInnerConnection != null) {
            unbindService(mInnerConnection);
            mInnerConnection = null;
        }


    }


    public static void startService(Context context) {
        Intent intent = new Intent(context, GuardService.class);
        context.startService(intent);
    }

    public static void stopService(Context context) {
        Intent intent = new Intent(context, GuardService.class);
        context.stopService(intent);
    }

    public static Intent getExplicitIntent(Context context, Intent implicitIntent) {

        if (context.getApplicationInfo().targetSdkVersion < Build.VERSION_CODES.LOLLIPOP) {
            return implicitIntent;
        }

        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
        // Set the component to be explicit
        explicitIntent.setComponent(component);
        return explicitIntent;
    }
}
