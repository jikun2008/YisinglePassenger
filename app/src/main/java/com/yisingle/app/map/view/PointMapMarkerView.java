package com.yisingle.app.map.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.yisingle.app.R;
import com.yisingle.app.map.utils.CircleBuilder;
import com.yisingle.app.map.utils.MarkerBuilder;
import com.yisingle.app.utils.AnimatorUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jikun on 17/6/11.
 */

public class PointMapMarkerView extends BaseMapMarkerView {

    protected Marker textMarker = null;


    private View infoWindow = null;


    private List<Circle> circleList;

    private int size;

    private String text;


    private ValueAnimator valueAnimator;

    private String time;


    private TextView tv_info;

    private TextView tv_time;

    private LinearLayout ll_waitTime;

    private LinearLayout ll_left;


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public PointMapMarkerView(Context mContext, int size) {
        super(mContext);
        this.size = size;
    }

    @Override
    public boolean isAddMarkViewToMap() {
        return currentMarker != null;
    }


    public void addMarkViewToMap(LatLng latLng, @DrawableRes int res, AMap aMap) {
        addMarkViewToMap(latLng, res, aMap, false, "");

    }


    public void addMarkViewToMap(LatLng latLng, @DrawableRes int res, AMap aMap, boolean ishowInfoWindow, String time) {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                res);
        this.time = time;
        currentMarker = MarkerBuilder.getStartMarkerToMapView(latLng,
                bitmap, aMap);
        initMarkInfoWindowAdapter(aMap);
        if (ishowInfoWindow) {
            currentMarker.showInfoWindow();
        }

    }

    int currentValue = 0;

    public void addCircleViewToMap(LatLng latlng, AMap aMap) {
        circleList = new ArrayList<>();
        int radius = 50;


        for (int i = 0; i < 4; i++) {
            radius = radius + 50 * i;
            circleList.add(CircleBuilder.addCircle(latlng, radius, aMap));
        }


        valueAnimator = AnimatorUtils.getValueAnimator(0, 50, animation -> {

            int value = (int) animation.getAnimatedValue();
            if (currentValue == value) {
                //防止闪烁的方式
                return;
            }
            Log.e("测试代码", "测试代码value=" + value);


            for (int i = 0; i < circleList.size(); i++) {
                int nowradius = 50 + 50 * i;

                Circle circle = circleList.get(i);
                double radius1 = value + nowradius;
                circle.setRadius(radius1);
                int strokePercent = 200;
                int fillPercent = 20;
                if (value < 25) {
                    strokePercent = value * 8;
                    fillPercent = value * 20 / 50;
                } else {
                    strokePercent = 200 - value * 4;
                    fillPercent = 20 - value * 20 / 50;
                }

                Log.e("测试代码", "测试代码strokePercent=" + strokePercent + "--fillPercent" + fillPercent);
                if (circle.getFillColor() != CircleBuilder.getStrokeColor(strokePercent)) {
                    circle.setStrokeColor(CircleBuilder.getStrokeColor(strokePercent));
                    circle.setFillColor(CircleBuilder.getFillColor(fillPercent));
                }


            }
            currentValue = value;

        });


    }

    public void stopAnimator() {
        if (null != valueAnimator) {
            valueAnimator.cancel();
        }

    }

    public void moveToCamera(AMap aMap, LatLng latLng) {
        float zoom = 17;//设置缩放级别
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));//zoom - 缩放级别，[3-20]。
    }

    public void addMarkViewToMap(String text, LatLng latLng, @DrawableRes int res, AMap aMap, boolean ishowInfoWindow) {


        this.text = text;
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                res);
        currentMarker = MarkerBuilder.getStartMarkerToMapView(latLng,
                bitmap, aMap);
        initMarkInfoWindowAdapter(aMap);
        if (ishowInfoWindow) {
            currentMarker.showInfoWindow();
        }
        textMarker = MarkerBuilder.getTextToMapView(text, latLng, aMap, size);
    }

    @Override
    public void removeMarkerViewFromMap() {
        super.removeMarkerViewFromMap();
        if (null != textMarker) {
            textMarker.destroy();
            textMarker = null;
        }
        stopAnimator();
        if (circleList != null) {
            for (Circle circle : circleList) {
                circle.remove();
            }
            circleList.clear();
        }


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
                            R.layout.map_wait_car_info_window, null);
                    tv_time = (TextView) infoWindow.findViewById(R.id.tv_time);
                    tv_info = (TextView) infoWindow.findViewById(R.id.tv_info);
                    ll_waitTime = (LinearLayout) infoWindow.findViewById(R.id.ll_waitTime);
                    ll_left = (LinearLayout) infoWindow.findViewById(R.id.ll_left);
                    if (TextUtils.isEmpty(time)) {
                        reshInfoWindowData();
                    } else {
                        reshTimeInfoWindowData(time);
                    }

                }


                return infoWindow;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
    }


    public void reshInfoWindowData() {
        tv_info.setText("从这里出发");


        ll_waitTime.setVisibility(View.GONE);

        ll_left.setVisibility(View.VISIBLE);
    }


    public void reshTimeInfoWindowData(String time) {
        this.time = time;
        tv_info.setText("正在为你寻找车辆");
        tv_time.setText(time);
        ll_waitTime.setVisibility(View.VISIBLE);

        ll_left.setVisibility(View.GONE);
    }
}
