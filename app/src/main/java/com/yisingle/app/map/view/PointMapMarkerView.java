package com.yisingle.app.map.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.yisingle.app.R;
import com.yisingle.app.map.utils.CircleBuilder;
import com.yisingle.app.map.utils.MarkerBuilder;
import com.yisingle.app.utils.AnimatorUtils;
import com.yisingle.app.utils.TimeUtils;
import com.yisingle.baselibray.baseadapter.viewholder.MapInfoWindowViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by jikun on 17/6/11.
 */

public class PointMapMarkerView extends BaseMapMarkerView<PointMapMarkerView.PointMapMarkerData, PointMapMarkerView.PointMapWindowData> {

    protected Marker textMarker = null;


    private List<Circle> circleList;


    private ValueAnimator valueAnimator;


    Subscription timesubscription;

    private int currentValue;


    public PointMapMarkerView(AMap aMap, Context context) {
        super(aMap, context);

    }


    public void addCircleViewToMap(LatLng latlng, AMap aMap) {
        circleList = new ArrayList<>();
        int radius = 50;


        for (int i = 0; i < 4; i++) {
            radius = radius + 50 * i;
            circleList.add(CircleBuilder.addCircle(latlng, radius, aMap));
        }


        valueAnimator = AnimatorUtils.getValueAnimator(0, 50, animation -> {

            int value = (int) animation.getAnimatedValue();


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


                if (circle.getFillColor() != CircleBuilder.getStrokeColor(strokePercent)) {
                    circle.setStrokeColor(CircleBuilder.getStrokeColor(strokePercent));
                    circle.setFillColor(CircleBuilder.getFillColor(fillPercent));
                }


            }
            currentValue = value;

        });


    }

    public void removeCircle() {
        stopAnimator();
        if (circleList != null) {
            for (Circle circle : circleList) {
                circle.remove();
            }
            circleList.clear();
        }
    }


    public void stopCountTime() {
        if (null != timesubscription && !timesubscription.isUnsubscribed()) {
            timesubscription.unsubscribe();
        }

        if (null != getMarker()) {
            getMarker().hideInfoWindow();
        }


    }

    private void stopAnimator() {
        if (null != valueAnimator) {
            valueAnimator.cancel();
        }

    }

    public void moveToCamera() {
        float zoom = 17;//设置缩放级别
        if (null != getMap() && null != getMarker()) {
            getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(getMarker().getPosition(), zoom));//zoom - 缩放级别，[3-20]。
        }
    }


    public void startCountTime() {
        timesubscription = Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                    String time = TimeUtils.millis2String(aLong * 1000, sdf);
                    PointMapWindowData data = PointMapWindowData.createTimeData(true, time);
                    reshInfoWindowData(data);
                });
    }


    @Override
    protected Marker buildMarker(PointMapMarkerData markerData) {

        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                markerData.getDrawableId());
        Marker marker = MarkerBuilder.getStartMarkerToMapView(markerData.getLatLng(),
                bitmap, getMap());
        if (markerData.ishowInfoWindow()) {
            getMarker().showInfoWindow();
        }
        textMarker = MarkerBuilder.getTextToMapView(markerData.getText(), markerData.getLatLng(), getMap(), markerData.getSize());
        return marker;
    }

    @Override
    public void removeView() {
        super.removeView();
        if (null != textMarker) {
            textMarker.destroy();
            textMarker = null;
        }
        removeCircle();
        stopCountTime();


    }


    public void initMarkInfoWindowAdapter() {
        initMarkInfoWindowAdapter(PointMapWindowData.createTimeData(true, "00:00"), R.layout.map_wait_car_info_window, new InfoWindowListener<PointMapWindowData>() {
            @Override
            public void bindData(MapInfoWindowViewHolder viewHolder, PointMapWindowData data) {
                if (data.ishowTimeView()) {
                    viewHolder.setText(R.id.tv_info, data.getInfo());
                    viewHolder.setText(R.id.tv_time, data.getTime());
                    viewHolder.setVisibility(R.id.ll_waitTime, View.VISIBLE);
                    viewHolder.setVisibility(R.id.ll_left, View.GONE);
                } else {
                    viewHolder.setText(R.id.tv_info, data.getInfo());
                    viewHolder.setVisibility(R.id.ll_waitTime, View.GONE);
                    viewHolder.setVisibility(R.id.ll_left, View.VISIBLE);
                }
            }
        });
    }


    public static class PointMapMarkerData extends BaseMarkerData {
        private boolean ishowInfoWindow;
        private String text;
        private int size;


        private PointMapMarkerData() {

        }

        public boolean ishowInfoWindow() {
            return ishowInfoWindow;
        }

        public void setIshowInfoWindow(boolean ishowInfoWindow) {
            this.ishowInfoWindow = ishowInfoWindow;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }


        public static PointMapMarkerData createData(@DrawableRes int drawableId, LatLng latLng, String text, int size) {
            PointMapMarkerData pointMapMarkerData = new PointMapMarkerData();
            pointMapMarkerData.setText(text);
            pointMapMarkerData.setSize(size);
            pointMapMarkerData.setLatLng(latLng);
            pointMapMarkerData.setDrawableId(drawableId);
            return pointMapMarkerData;
        }
    }

    public static class PointMapWindowData extends BaseWindowData {

        private boolean ishowTimeView;
        private String time;
        private String info;

        private PointMapWindowData() {


        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public boolean ishowTimeView() {
            return ishowTimeView;
        }

        public void setIshowTimeView(boolean ishowTimeView) {
            this.ishowTimeView = ishowTimeView;
        }

        public static PointMapWindowData createNoTimeData(boolean isShowWindow) {
            PointMapWindowData data = createData(isShowWindow, "", "从这里出发", false);
            return data;
        }

        public static PointMapWindowData createTimeData(boolean isShowWindow, String time) {
            PointMapWindowData data = createData(isShowWindow, time, "正在为你寻找车辆", true);
            return data;
        }

        public static PointMapWindowData createData(boolean isShowWindow, String time, String info, boolean ishowTimeView) {
            PointMapWindowData data = new PointMapWindowData();
            data.setShowWindow(isShowWindow);
            data.setShowWindow(true);
            data.setIshowTimeView(ishowTimeView);
            data.setTime(time);
            data.setInfo(info);
            return data;
        }
    }
}
