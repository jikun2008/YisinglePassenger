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


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppManager.getInstance().init(this);
        Logger.init().methodCount(0).hideThreadInfo();

        CrashReport.initCrashReport(getApplicationContext(), "8856cf1479", false);

    }
}
