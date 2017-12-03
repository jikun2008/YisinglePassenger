package com.map.library.listener;

import android.util.Log;

import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.autonavi.tbt.TrafficFacilityInfo;

/**
 * Created by jikun on 2016/12/23.
 * 导航监听适配器，只实现需要的方法
 */

public abstract class AMapNaviAdapterListener implements AMapNaviListener {


    public final String TAG = AMapNaviAdapterListener.class.getSimpleName();

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int type) {
        //开始导航回调
        Log.e(TAG, TAG + ":开始导航回调-onStartNavi" + type);
    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {
        //
        Log.e(TAG, TAG + ":onTrafficStatusUpdate");
    }

    @Override
    public void onLocationChange(AMapNaviLocation location) {
        //当前位置回调
        Log.e(TAG, TAG + ":当前位置回调-onLocationChange");
    }

    @Override
    public void onGetNavigationText(int type, String text) {
        //播报类型和播报文字回调
        Log.e(TAG, TAG + ":播报类型和播报文字回调-onGetNavigationText");
    }

    @Override
    public void onEndEmulatorNavi() {
        //结束模拟导航
        Log.e(TAG, TAG + ":结束模拟导航-onEndEmulatorNavi");
    }

    @Override
    public void onArriveDestination() {
        //到达目的地
        Log.e(TAG, TAG + ":到达目的地-onArriveDestination");
    }


    @Override
    public void onReCalculateRouteForYaw() {
        //偏航后重新计算路线回调
        Log.e(TAG, TAG + ":偏航后重新计算路线回调-onReCalculateRouteForYaw");
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        //拥堵后重新计算路线回调
        Log.e(TAG, TAG + ":拥堵后重新计算路线回调-onReCalculateRouteForTrafficJam");
    }

    @Override
    public void onArrivedWayPoint(int wayID) {
        //到达途径点
        Log.e(TAG, TAG + ":到达途径点-onArrivedWayPoint");
    }

    @Override
    public void onGpsOpenStatus(boolean enabled) {
        //GPS开关状态回调
        Log.e(TAG, TAG + ":GPS开关状态回调-onGpsOpenStatus(enabled)=" + enabled);
    }


    @Deprecated
    @Override
    public void onNaviInfoUpdated(AMapNaviInfo naviInfo) {
        //过时
    }


    @Override
    public void onNaviInfoUpdate(NaviInfo naviinfo) {
        //导航过程中的信息更新，请看NaviInfo的具体说明
        Log.e(TAG, TAG + ":导航过程中的信息更新，请看NaviInfo的具体说明--onNaviInfoUpdate");
    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
        //已过时
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
        //已过时
    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
        //显示转弯回调
    }

    @Override
    public void hideCross() {
        //隐藏转弯回调
    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] laneInfos, byte[] laneBackgroundInfo, byte[] laneRecommendedInfo) {
        //显示车道信息

    }

    @Override
    public void hideLaneInfo() {
        //隐藏车道信息
    }



    @Override
    public void notifyParallelRoad(int i) {
        // 通知当前是否显示平行路切换   parallelRoadType - 0表示隐藏 1 表示显示主路 2 表示显示辅路

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
        //更新交通设施信息
        Log.e(TAG, TAG + ":更新交通设施信息--OnUpdateTrafficFacility");
    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
        //更新巡航模式的统计信息
        Log.e(TAG, TAG + ":更新巡航模式的统计信息--updateAimlessModeStatistics");
    }


    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
        //更新巡航模式的拥堵信息
        Log.e(TAG, TAG + ":更新巡航模式的拥堵信息--updateAimlessModeCongestionInfo");
    }


}
