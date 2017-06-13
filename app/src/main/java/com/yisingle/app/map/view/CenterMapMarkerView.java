package com.yisingle.app.map.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.IntRange;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.yisingle.app.R;
import com.yisingle.app.utils.BitMapUtils;

import java.util.ArrayList;

/**
 * Created by jikun on 17/5/17.
 * 地图中选点帮助类
 */

public class CenterMapMarkerView extends BaseMapMarkerView {


    private View infoWindow = null;

    private boolean infowindowloading = false;

    public CenterMapMarkerView(Context mContext) {
        super(mContext);
    }



    public void addMarkViewToMap(AMap aMap) {
        if (currentMarker == null) {

            initMarkInfoWindowAdapter(aMap);

            currentMarker = MarkerBuilder.getCenterMarkerToMapView(getSingleBitmapDescriptor(), aMap);
        }
    }


    public void showInfoWindowLoading() {
        infowindowloading = true;
        if (!currentMarker.isInfoWindowShown()) {
            currentMarker.showInfoWindow();
        } else {
            updateInfoWindow();
        }

    }

    public void stopInfoWindowLoading() {
        infowindowloading = false;
        if (null == currentMarker) return;
        if (!currentMarker.isInfoWindowShown()) {
            currentMarker.showInfoWindow();
        } else {
            updateInfoWindow();
        }

    }


    public void hideInfoWindow() {
        if (null != currentMarker) {
            currentMarker.hideInfoWindow();
        }
    }

    /**
     * 开启帧动画
     * 设置多少帧刷新一次图片资源，Marker动画的间隔时间，值越小动画越快。默认为20，最小为1
     */
    public void startFrameAnimation(@IntRange(from = 1, to = 20) int time) {
        if (null != currentMarker) {
            currentMarker.setIcons(getListBitmapDescriptor());
            currentMarker.setPeriod(time);// 设置多少帧刷新一次图片资源，Marker动画的间隔时间，值越小动画越快。默认为20，最小为1。
            currentMarker.setTitle("center");//如果要显示InfoWindow(无论是否自定义)一定要设置Title否则无论你怎么设置都不会显示infowindow


        }

    }


    public void stopFrameAnimation() {
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
                }

                updateInfoWindow();


                return infoWindow;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
    }

    private void updateInfoWindow() {
        LinearLayout ll_left = (LinearLayout) infoWindow.findViewById(R.id.ll_left);
        ll_left.setVisibility(infowindowloading ? View.GONE : View.VISIBLE);
        ImageView iv_loading = (ImageView) infoWindow.findViewById(R.id.iv_loading);
        iv_loading.setVisibility(infowindowloading ? View.VISIBLE : View.GONE);
        AnimationDrawable animationDrawable = (AnimationDrawable) iv_loading.getDrawable();

        if (infowindowloading) {

            if (!animationDrawable.isRunning()) {
                animationDrawable.start();
            }

        } else {
            if (animationDrawable.isRunning()) {
                animationDrawable.stop();
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
