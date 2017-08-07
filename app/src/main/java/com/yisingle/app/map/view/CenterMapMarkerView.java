package com.yisingle.app.map.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.util.Log;
import android.view.View;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.map.library.view.base.BaseMapMarkerView;
import com.map.library.view.base.BaseMarkerData;
import com.map.library.view.base.BaseWindowData;
import com.yisingle.app.R;
import com.yisingle.app.map.utils.MarkerBuilder;
import com.yisingle.app.map.view.CenterMapMarkerView.CenterWindowData;
import com.yisingle.app.utils.BitMapUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;


/**
 * Created by jikun on 17/5/17.
 * 地图中选点帮助类
 */

public class CenterMapMarkerView extends BaseMapMarkerView<BaseMarkerData, CenterWindowData> {


    public CenterMapMarkerView(AMap aMap, Context context) {
        super(aMap, context);
    }

    @Override
    protected Marker buildMarker(BaseMarkerData markerData) {
        Marker marker = MarkerBuilder.getCenterMarkerToMapView(getSingleBitmapDescriptor(), getMap());

        return marker;
    }

    @Override
    protected void addMarkSuccess(BaseMarkerData markerData) {

    }


    public void initMarkInfoWindowAdapter() {
        initMarkInfoWindowAdapter(CenterWindowData.createLoading(), R.layout.map_fast_car_info_window, (viewHolder, data) -> {
            if (data.getType() == CenterWindowData.Type.LOADING) {
                Log.e("测试代码", "测试代码bindData------LOADING");
                viewHolder.setVisibility(R.id.ll_have_net, View.VISIBLE)
                        .setVisibility(R.id.ll_no_net, View.GONE)
                        .setVisibility(R.id.ll_left, View.GONE)
                        .setVisibility(R.id.iv_loading, View.VISIBLE)
                        .startAnimationDrawable(R.id.iv_loading);
            } else if (data.getType() == CenterWindowData.Type.SUCCESS) {
                Log.e("测试代码", "测试代码bindData------SUCCESS");

                viewHolder.setVisibility(R.id.ll_have_net, View.VISIBLE)
                        .setVisibility(R.id.ll_no_net, View.GONE)
                        .setVisibility(R.id.ll_left, View.VISIBLE)
                        .setVisibility(R.id.iv_loading, View.GONE)
                        .stopAnimationDrawable(R.id.iv_loading);
            } else {
                Log.e("测试代码", "测试代码bindData------Failed");
                viewHolder.setVisibility(R.id.ll_have_net, View.GONE)
                        .setVisibility(R.id.ll_no_net, View.VISIBLE)
                        .stopAnimationDrawable(R.id.iv_loading);
            }

        });
    }

    public void showLoading(@IntRange(from = 1, to = 20) int time) {
        startFrameAnimation(time);
        if (null != getMarker() && !getMarker().isInfoWindowShown()) {
            getMarker().showInfoWindow();
        }
        reshInfoWindowData(CenterWindowData.createLoading());
    }

    public void stopLoading() {
        stopFrameAnimation();
    }


    public void hideInfoWindow() {
        if (null != getMarker() && getMarker().isInfoWindowShown()) {
            setInfoData(CenterWindowData.createLoading());
            getMarker().hideInfoWindow();
        }
    }

    /**
     * 开启帧动画
     * 设置多少帧刷新一次图片资源，Marker动画的间隔时间，值越小动画越快。默认为20，最小为1
     */
    private void startFrameAnimation(@IntRange(from = 1, to = 20) int time) {
        if (null != getMarker()) {
            getMarker().setIcons(getListBitmapDescriptor());
            getMarker().setPeriod(time);// 设置多少帧刷新一次图片资源，Marker动画的间隔时间，值越小动画越快。默认为20，最小为1。
        }

    }


    private void stopFrameAnimation() {
        if (null != getMarker()) {
            getMarker().setIcon(getSingleBitmapDescriptor());
        }
    }


    private BitmapDescriptor getSingleBitmapDescriptor() {
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                R.mipmap.icon_me_location);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private ArrayList<BitmapDescriptor> getListBitmapDescriptor() {
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                R.mipmap.icon_me_location);
        return BitMapUtils.getMultipleBitmapDescriptorList(bitmap);
    }

    public static class CenterWindowData extends BaseWindowData {


        @Type
        int type;

        //添加支持注解的依赖到你的项目中，需要在build.gradle文件中的依赖块中添加：
        //dependencies { compile 'com.android.support:support-annotations:24.2.0' }
        @IntDef({Type.LOADING, Type.SUCCESS, Type.ERROR})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Type {

            int LOADING = 0;
            int SUCCESS = 1;
            int ERROR = 2;


        }

        public static CenterWindowData createLoading() {
            CenterWindowData centerWindowData = new CenterWindowData();
            centerWindowData.setType(Type.LOADING);
            return centerWindowData;
        }

        public static CenterWindowData createSuccess() {
            CenterWindowData centerWindowData = new CenterWindowData();
            centerWindowData.setType(Type.SUCCESS);
            return centerWindowData;
        }

        public static CenterWindowData createError() {
            CenterWindowData centerWindowData = new CenterWindowData();
            centerWindowData.setType(Type.ERROR);
            return centerWindowData;
        }

        public
        @Type
        int getType() {
            return type;
        }

        public void setType(@Type int type) {
            this.type = type;
        }
    }

}
