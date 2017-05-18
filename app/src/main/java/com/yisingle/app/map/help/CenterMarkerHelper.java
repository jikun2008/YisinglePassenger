package com.yisingle.app.map.help;

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
import com.amap.api.maps.model.Marker;
import com.yisingle.app.R;
import com.yisingle.app.map.utils.BitMapUtils;
import com.yisingle.app.map.view.MarkerBuilder;

import java.util.ArrayList;

/**
 * Created by jikun on 17/5/17.
 * 地图中选点帮助类
 */

public class CenterMarkerHelper {
    private Marker centerMarker = null;

    private Context mContext;

    private View infoWindow = null;

    private boolean infowindowloading = false;

    public CenterMarkerHelper(Context context) {
        mContext = context;


    }

    /**
     * 初始化加载对话框
     *
     * @param aMap  高德地图的Amap类
     */
    public void initMarkInfoWindowAdapter(AMap aMap) {

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

    public void destroy() {
        if (null != centerMarker) {
            centerMarker.destroy();
            centerMarker = null;
        }
    }


    public void addCenterMarkToMap(AMap aMap) {

        if (centerMarker == null) {
            centerMarker = MarkerBuilder.getCenterMarkerToMapView(geSingleBitmapDescriptor(), aMap);
        }
    }

    /**
     * 开启帧动画
     * 设置多少帧刷新一次图片资源，Marker动画的间隔时间，值越小动画越快。默认为20，最小为1
     */
    public void startFrameAnimation(@IntRange(from = 1, to = 20) int time) {
        if (null != centerMarker) {

            centerMarker.setIcons(getMultipleBitmapDescriptorList());
            centerMarker.setPeriod(time);// 设置多少帧刷新一次图片资源，Marker动画的间隔时间，值越小动画越快。默认为20，最小为1。
            centerMarker.setTitle("center");//如果要显示InfoWindow(无论是否自定义)一定要设置Title否则无论你怎么设置都不会显示infowindow
            showInfoWindowLoading();

        }

    }


    public void stopFrameAnimation() {
        if (null != centerMarker) {
            centerMarker.setIcon(geSingleBitmapDescriptor());
        }
    }


    public void hideInfoWindow() {
        if (centerMarker != null) {
            centerMarker.hideInfoWindow();
        }
    }

    private void showInfoWindowLoading() {
        infowindowloading = true;
        if (!centerMarker.isInfoWindowShown()) {
            centerMarker.showInfoWindow();
        } else {
            updateInfoWindow();
        }

    }

    public void showInfoWindowCompleteData(String data) {
        infowindowloading = false;
        if (!centerMarker.isInfoWindowShown()) {
            centerMarker.showInfoWindow();
        } else {
            updateInfoWindow();
        }
    }

    /**
     * 获取多个BitmapDescriptor的列表
     * 用来设置给Marker进行gif动画
     *
     * @return   返回中心Marker的动画效果图片组
     */
    private ArrayList<BitmapDescriptor> getMultipleBitmapDescriptorList() {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                R.mipmap.icon_me_location);
        ArrayList<BitmapDescriptor> list = new ArrayList<>();
        list.add(BitMapUtils.combineCenterBitmapDescriptor(bitmap, 0f));
        list.add(BitMapUtils.combineCenterBitmapDescriptor(bitmap, 0.2f));
        list.add(BitMapUtils.combineCenterBitmapDescriptor(bitmap, 0.4f));
        list.add(BitMapUtils.combineCenterBitmapDescriptor(bitmap, 0.6f));
        list.add(BitMapUtils.combineCenterBitmapDescriptor(bitmap, 0.8f));
        list.add(BitMapUtils.combineCenterBitmapDescriptor(bitmap, 0.9f));
        return list;

    }

    /**
     * 获取单个BitmapDescriptor
     * 用阿里设置给Marker
     *
     * @return   返回中间marker的单个图片
     */
    private BitmapDescriptor geSingleBitmapDescriptor() {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                R.mipmap.icon_me_location);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @SuppressWarnings("unused")
    public Marker getCenterMarker() {
        return centerMarker;
    }

    @SuppressWarnings("unused")
    public void setCenterMarker(Marker centerMarker) {
        this.centerMarker = centerMarker;
    }
}
