package com.yisingle.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by jikun on 17/5/12.
 */

public class MainApplication extends Application {

    private static Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        context = getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppManager.getInstance().init(this);
        Logger.init().methodCount(0).hideThreadInfo();

        CrashReport.initCrashReport(getApplicationContext(), "8856cf1479", false);

    }

    public static Context getContext() {

        return context;

    }
}
