package com.map.library.listener;

import android.util.Log;

import com.amap.api.navi.AMapNaviViewListener;

/**
 * Created by jikun on 2016/12/23.
 * 导航View适配器
 */

public abstract class AMapNaviViewAdapterListener implements AMapNaviViewListener {

    public String TAG=AMapNaviViewAdapterListener.class.getSimpleName();
    @Override
    public void onNaviSetting() {
        //底部导航设置点击回调
        Log.e(TAG, TAG + ":底部导航设置点击回调-onNaviSetting");
    }

    @Override
    public void onNaviMapMode(int isLock) {
        //地图的模式，锁屏或锁车

    }

//    @Override
//    public void onNaviCancel() {
//        //finish();
//        Log.e(TAG, TAG + ":点击关闭按钮onNaviCancel");
//    }


    @Override
    public void onNaviTurnClick() {
        //转弯view的点击回调
        Log.e(TAG, TAG + ":转弯view的点击回调onNaviTurnClick");
    }

    @Override
    public void onNextRoadClick() {
        //下一个道路View点击回调
        Log.e(TAG, TAG + ":下一个道路View点击回调--onNextRoadClick");
    }


    @Override
    public void onScanViewButtonClick() {
        //全览按钮点击回调
        Log.e(TAG, TAG + ":全览按钮点击回调--onScanViewButtonClick");
    }


    @Override
    public void onLockMap(boolean isLock) {
        //锁地图状态发生变化时回调
        //Log.e(TAG,TAG+":地图状态发生变化时回调--onLockMap(isLock)--isLock"+isLock);
    }

    @Override
    public void onNaviViewLoaded() {
        Log.e(TAG, TAG + ":导航页面加载成功--onNaviViewLoaded");
        // Log.e(TAG,TAG+":请不要使用AMapNaviView.getMap().setOnMapLoadedListener();会overwrite导航SDK内部画线逻辑");
        Log.e("wlx", "导航页面加载成功");
        Log.e("wlx", "请不要使用AMapNaviView.getMap().setOnMapLoadedListener();会overwrite导航SDK内部画线逻辑");
    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }
}
