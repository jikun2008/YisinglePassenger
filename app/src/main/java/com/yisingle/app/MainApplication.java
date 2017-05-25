package com.yisingle.app;

import android.app.Application;

import com.orhanobut.logger.Logger;

/**
 * Created by jikun on 17/5/12.
 */

public class MainApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        AppManager.getInstance().init(this);
        Logger.init().methodCount(0).hideThreadInfo();

    }
}
