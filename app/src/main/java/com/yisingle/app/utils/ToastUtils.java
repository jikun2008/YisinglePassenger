package com.yisingle.app.utils;

import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.yisingle.app.AppManager;


/**
 * Created by tanchuanzhi on 2015/5/8.
 */
public class ToastUtils {

    public static void show(CharSequence message) {
        show(message, Toast.LENGTH_SHORT);
    }

    public static void show(CharSequence message, int duration) {
        if (TextUtils.isEmpty(message))
            return;
        Toast toast = Toast.makeText(AppManager.appContext(), message, duration);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            toast.show();
        } else {
            HandlerUtils.runOnUI(() -> toast.show());
        }
    }

}
