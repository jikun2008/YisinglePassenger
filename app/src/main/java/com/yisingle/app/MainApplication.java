package com.yisingle.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.Utils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tencent.bugly.Bugly;

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
        initLogger();

        Utils.init(getApplicationContext());//初始化AndroidUtilCode


        Bugly.init(getApplicationContext(), "8856cf1479", false);//使用腾讯Bugly 开进行升级  保存bug

    }

    public static Context getContext() {

        return context;

    }

    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(0)        // (Optional) Hides internal method calls up to offset. Default 5
//                .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("My custom tag")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }
}
