package com.yisingle.app.base;

import android.app.Application;

import com.orhanobut.logger.Logger;

/**
 * Created by jikun on 17/5/12.
 */

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Logger.init().methodCount(0).hideThreadInfo();

    }
}
