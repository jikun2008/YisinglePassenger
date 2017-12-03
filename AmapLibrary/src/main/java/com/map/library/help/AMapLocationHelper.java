package com.map.library.help;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.orhanobut.logger.Logger;

/**
 * Created by jikun on 2016/12/12.
 */


public class AMapLocationHelper {


    private boolean is_default_once_location = false;//默认不使用一次定位

    private final int time_location_interval = 2000;//定位间隔,多次定位时才有效

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();


    private OnLocationGetListener onLocationGetListener;


    public AMapLocationHelper(Context context) {
        initLocation(context);

    }

    /**
     * 初始化定位
     *
     * @since 2.8.0
     */
    private void initLocation(Context context) {
        //初始化client
        locationClient = new AMapLocationClient(context);
        //设置定位参数
        locationOption = getDefaultOption(is_default_once_location, time_location_interval);
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }


    /**
     * 默认的定位参数
     *
     * @since 2.8.0
     * isOnceLocation 是否是单次定位
     * interval   定位间隔  如果设置isOnceLocation=true  interval无效
     */
    private AMapLocationClientOption getDefaultOption(boolean isOnceLocation, int interval) {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(5000);//可选，设置网络请求超时时间。默认为5秒。在仅设备模式下无效
        mOption.setInterval(interval);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(isOnceLocation);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        //mOption.setOnceLocationLatest(false);
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        return mOption;
    }


    /**
     * 开始多次定位 默认时间2S
     *
     * @since 2.8.0
     */
    public void startLocation() {
        if (null != locationClient) {

            locationOption = getDefaultOption(false, time_location_interval);
            // 设置定位参数
            locationClient.setLocationOption(locationOption);
            // 启动定位
            locationClient.startLocation();
        }

    }

    /**
     * 开始多次定位
     *
     * @since 2.8.0
     */
    public void startLocation(int time) {
        if (null != locationClient) {

            locationOption = getDefaultOption(false, time);
            // 设置定位参数
            locationClient.setLocationOption(locationOption);
            // 启动定位
            locationClient.startLocation();
        }

    }

    /**
     * 停止定位，可以跟多次定位配合使用
     *
     * @since 2.8.0
     */
    public void stopLocation() {
        // 停止定位
        if (null != locationClient) {
            locationClient.stopLocation();
        }

    }

    /**
     * 开启单次定位
     */
    public void startSingleLocate() {


        if (null != locationClient) {
            locationClient.setLocationListener(locationListener);
            locationOption = getDefaultOption(true, time_location_interval);//当isOnceLocation为true的时候设置time_location_interval没有作用，因为是单次定位
            locationClient.setLocationOption(locationOption);
            locationClient.startLocation();
        }
    }

    /**
     * 开启单次定位
     */
    public void startSingleLocate(OnLocationGetListener onLocationGetListener) {
        this.onLocationGetListener = onLocationGetListener;

        if (null != locationClient) {
            locationClient.stopLocation();
            locationClient.setLocationListener(locationListener);
            locationOption = getDefaultOption(true, time_location_interval);//当isOnceLocation为true的时候设置time_location_interval没有作用，因为是单次定位
            locationClient.setLocationOption(locationOption);
            locationClient.startLocation();
        }

    }

    /**
     * 开启单次定位
     */
    public void startSingleLocateNoStop(OnLocationGetListener onLocationGetListener) {
        this.onLocationGetListener = onLocationGetListener;

        if (null != locationClient) {
            //locationClient.stopLocation();
            locationClient.setLocationListener(locationListener);
            locationOption = getDefaultOption(true, time_location_interval);//当isOnceLocation为true的时候设置time_location_interval没有作用，因为是单次定位
            locationClient.setLocationOption(locationOption);
            locationClient.startLocation();
        }

    }

    /**
     * 销毁定位
     * 如果AMapLocationClient是在当前Activity实例化的，
     * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
     *
     * @since 2.8.0
     */
    public void destroyLocation() {
        if (null != locationClient) {
            locationClient.stopLocation();
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
            onLocationGetListener = null;
        }
    }


    private static AMapLocationClient LastKnowAMapLocationClient = null;

    /**
     * 获取最后一次定位的位置
     *
     * @return 返回最后一次定位的位置
     */
    public static AMapLocation getLastKnownLocation(Context context) {

        //初始化client

        if (LastKnowAMapLocationClient == null) {
            LastKnowAMapLocationClient = new AMapLocationClient(context);
        }

        return LastKnowAMapLocationClient.getLastKnownLocation();

    }


    /**
     * 定位监听
     */
    private AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            Logger.d("Location---onLocationChanged");
            if (loc != null && loc.getErrorCode() == 0) {

                if (null != onLocationGetListener) {
                    onLocationGetListener.onLocationGetSuccess(loc);
                }


            } else {
                if (null != onLocationGetListener) {
                    onLocationGetListener.onLocationGetFail(loc);
                }
            }
        }
    };


    public void setOnLocationGetListener(OnLocationGetListener onLocationGetListener) {
        this.onLocationGetListener = onLocationGetListener;
    }

    public static abstract class OnLocationGetListeneAdapter implements OnLocationGetListener {


        @Override
        public void onLocationGetFail(AMapLocation loc) {

        }
    }

    public interface OnLocationGetListener {

        void onLocationGetSuccess(AMapLocation loc);

        void onLocationGetFail(AMapLocation loc);
    }


}
