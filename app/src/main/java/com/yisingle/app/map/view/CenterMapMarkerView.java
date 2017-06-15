package com.yisingle.app.map.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.IntRange;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.yisingle.app.R;
import com.yisingle.app.map.utils.MarkerBuilder;
import com.yisingle.app.utils.BitMapUtils;

import java.util.ArrayList;

/**
 * Created by jikun on 17/5/17.
 * 地图中选点帮助类
 */

public class CenterMapMarkerView extends BaseMapMarkerView {


    private View infoWindow = null;


    LinearLayout ll_have_net;
    LinearLayout ll_no_net;
    LinearLayout ll_left;
    ImageView iv_loading;

    public CenterMapMarkerView(Context mContext) {
        super(mContext);
    }


    public void addMarkViewToMap(AMap aMap) {
        if (currentMarker == null) {

            initMarkInfoWindowAdapter(aMap);

            currentMarker = MarkerBuilder.getCenterMarkerToMapView(getSingleBitmapDescriptor(), aMap);
            currentMarker.setTitle("center");//如果要显示InfoWindow(无论是否自定义)一定要设置Title否则无论你怎么设置都不会显示infowindow
        }
    }


    public void showLoading(@IntRange(from = 1, to = 20) int time) {
        startFrameAnimation(time);
        if (null != currentMarker && !currentMarker.isInfoWindowShown()) {
            currentMarker.showInfoWindow();
        }
        updateLoadingInfoWindow();
    }

    public void stopLoading() {
        stopFrameAnimation();
    }


    public void updateErrorInfoWindow() {
        if (ll_have_net != null) {
            Log.e("测试代码","测试代码Error----------updateErrorInfoWindow");
            ll_have_net.setVisibility(View.GONE);
            ll_no_net.setVisibility(View.VISIBLE);
            AnimationDrawable animationDrawable = (AnimationDrawable) iv_loading.getDrawable();
            if (animationDrawable.isRunning()) {
                animationDrawable.stop();
            }
        }
    }

    public void updateSucccessInfoWindow() {
        if (ll_have_net != null) {
            Log.e("测试代码","测试代码Succcess----------updateSucccessInfoWindow");
            ll_have_net.setVisibility(View.VISIBLE);
            ll_no_net.setVisibility(View.GONE);
            ll_left.setVisibility(View.VISIBLE);
            iv_loading.setVisibility(View.GONE);
            AnimationDrawable animationDrawable = (AnimationDrawable) iv_loading.getDrawable();
            if (animationDrawable.isRunning()) {
                animationDrawable.stop();
            }
        }

    }

    public void hideInfoWindow() {
        if (null != currentMarker && currentMarker.isInfoWindowShown()) {
            currentMarker.hideInfoWindow();
        }
    }

    /**
     * 开启帧动画
     * 设置多少帧刷新一次图片资源，Marker动画的间隔时间，值越小动画越快。默认为20，最小为1
     */
    private void startFrameAnimation(@IntRange(from = 1, to = 20) int time) {
        if (null != currentMarker) {
            currentMarker.setIcons(getListBitmapDescriptor());
            currentMarker.setPeriod(time);// 设置多少帧刷新一次图片资源，Marker动画的间隔时间，值越小动画越快。默认为20，最小为1。
        }

    }


    private void stopFrameAnimation() {
        if (null != currentMarker) {
            currentMarker.setIcon(getSingleBitmapDescriptor());
        }
    }


    public Marker getCenterMarker() {
        return currentMarker;
    }


    /**
     * 初始化加载对话框
     *
     * @param aMap 高德地图的Amap类
     */
    private void initMarkInfoWindowAdapter(AMap aMap) {

        aMap.setOnMarkerClickListener(marker -> false);

        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @SuppressLint("InflateParams")
            @Override
            public View getInfoWindow(Marker marker) {

                if (infoWindow == null) {
                    infoWindow = LayoutInflater.from(mContext).inflate(
                            R.layout.map_fast_car_info_window, null);
                    ll_have_net = (LinearLayout) infoWindow.findViewById(R.id.ll_have_net);

                    ll_no_net = (LinearLayout) infoWindow.findViewById(R.id.ll_no_net);
                    ll_left = (LinearLayout) infoWindow.findViewById(R.id.ll_left);
                    iv_loading = (ImageView) infoWindow.findViewById(R.id.iv_loading);
                    updateLoadingInfoWindow();
                }




                return infoWindow;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
    }


    private void updateLoadingInfoWindow() {
        if (ll_have_net != null) {
            Log.e("测试代码","测试代码Loading----------updateLoadingInfoWindow");
            ll_have_net.setVisibility(View.VISIBLE);
            ll_no_net.setVisibility(View.GONE);
            ll_left.setVisibility(View.GONE);
            iv_loading.setVisibility(View.VISIBLE);
            AnimationDrawable animationDrawable = (AnimationDrawable) iv_loading.getDrawable();
            if (!animationDrawable.isRunning()) {
                animationDrawable.start();
            }
        }

    }


    private BitmapDescriptor getSingleBitmapDescriptor() {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                R.mipmap.icon_me_location);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private ArrayList<BitmapDescriptor> getListBitmapDescriptor() {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                R.mipmap.icon_me_location);
        return BitMapUtils.getMultipleBitmapDescriptorList(bitmap);
    }

}
