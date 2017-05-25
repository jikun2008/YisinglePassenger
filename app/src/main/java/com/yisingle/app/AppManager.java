package com.yisingle.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


/**
 * 需要在application里初始化，提供一些api用于app状态判断和activity栈管理等
 * <p>
 * <p>
 * Create on 16/5/7.
 */
public class AppManager {

    private static Context sContext;
    private static Activity currentActivity;

    private int activeCount = 0;// 当前活动的activity数
    private boolean isForground = false;// 应用是否在前台

    private AppManager() {
    }

    public static AppManager getInstance() {
        return IntstanceHolder.sIntance;
    }

    private static class IntstanceHolder {
        private static final AppManager sIntance = new AppManager();
    }

    public void init(Application application) {
        sContext = application.getApplicationContext();
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ActivityStack.add(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                activeCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                isForground = true;
                currentActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
                currentActivity = null;
            }

            @Override
            public void onActivityStopped(Activity activity) {
                activeCount--;
                if (activeCount <= 0)
                    isForground = false;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityStack.remove(activity);
            }
        });
    }

    /**
     * @return 全局context
     */
    public static Context appContext() {
        if (sContext == null)
            throw new IllegalStateException("AppManager must be init in application!");
        return sContext;
    }

    /**
     * @return 当前应用是否在前台
     */
    public boolean isForground() {
        return isForground;
    }

    public static boolean jump(Class<? extends Activity> clazz) {
        return jump(clazz, null, null, null);
    }

    public static boolean jump(Class<? extends Activity> clazz, String key, Serializable value) {
        return jump(clazz, key, null, value);
    }

    public static boolean jump(Class<? extends Activity> clazz, String key, Parcelable value) {
        return jump(clazz, key, value, null);
    }

    private static boolean jump(Class<? extends Activity> clazz, String key, Parcelable pvalue, Serializable svalue) {

        Activity context = getCurrentActicity();
        if (context == null)
            return false;

        Intent intent = new Intent(context, clazz);
        if (svalue != null)
            intent.putExtra(key, svalue);
        if (pvalue != null)
            intent.putExtra(key, pvalue);

        context.startActivity(intent);
        return true;
    }

    public static Activity getCurrentActicity() {
        return currentActivity == null ? ActivityStack.getCurrentActivity() : currentActivity;
    }

    public static boolean isTop(Class<? extends Activity> clazz) {
        return currentActivity.getClass().getName().equals(clazz.getName());
    }

    public static boolean isExists(Class<? extends Activity> clazz) {
        return ActivityStack.isExists(clazz);
    }

    public static void finishExcept(Class<? extends Activity> clazz) {
        ActivityStack.finishExcept(clazz);
    }

    public static void exitApp() {
        ActivityStack.exitApp();
    }


    /**
     * 模拟activity栈，对activity进行管理
     */
    private static class ActivityStack {
        private static final LinkedList<Activity> STACK = new LinkedList<>();

        // 入栈
        private static void add(Activity aty) {
            synchronized (ActivityStack.class) {
                STACK.addLast(aty);
            }
        }

        // 出栈
        private static void remove(Activity aty) {
            synchronized (ActivityStack.class) {
                if (STACK.contains(aty))
                    STACK.remove(aty);
            }
        }

        private static Activity getCurrentActivity() {
            return STACK.getLast();
        }

        private static boolean isExists(Class<? extends Activity> clazz) {
            for (Activity aty : STACK) {
                if (aty.getClass().getSimpleName().equals(clazz.getSimpleName()))
                    return true;
            }
            return false;
        }

        private static void exitApp() {
            synchronized (ActivityStack.class) {
                List<Activity> copy = new LinkedList<>(STACK);
                for (Activity aty : copy) {
                    aty.finish();
                }
                copy.clear();

                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }

        private static void finishExcept(Class<? extends Activity> clazz) {
            synchronized (ActivityStack.class) {
                List<Activity> copy = new LinkedList<>(STACK);
                for (Activity aty : copy) {
                    if (!aty.getClass().equals(clazz)) aty.finish();
                }
                copy.clear();
            }
        }
    }
}
