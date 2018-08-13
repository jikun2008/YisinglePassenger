package com.yisingle.app.view.map;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.LatLng;
import com.blankj.utilcode.util.TimeUtils;
import com.yisingle.amapview.lib.base.view.marker.BaseMarkerView;
import com.yisingle.amapview.lib.view.PointMarkerView;
import com.yisingle.amapview.lib.viewholder.MapInfoWindowViewHolder;
import com.yisingle.app.R;
import com.yisingle.app.map.utils.CircleBuilder;
import com.yisingle.app.map.view.PointCircleMapMarkerView;
import com.yisingle.app.utils.AnimatorUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * @author jikun
 * Created by jikun on 2018/8/10.
 */
public class SendOrderMarkerView {
    private PointMarkerView<PointCircleMapMarkerView.PointMapWindowData> pointMarkerView;

    private List<Circle> circleList;

    private ValueAnimator valueAnimator;

    private AMap aMap;

    Subscription timesubscription;

    public SendOrderMarkerView(Context context, AMap aMap) {
        this.aMap = aMap;
        pointMarkerView = new PointMarkerView.Builder(context, aMap).create();

        pointMarkerView.bindInfoWindowView(new BaseMarkerView.BaseInfoWindowView<PointCircleMapMarkerView.PointMapWindowData>(R.layout.map_wait_car_info_window, null) {
            @Override
            public void bindData(MapInfoWindowViewHolder viewHolder, PointCircleMapMarkerView.PointMapWindowData data) {
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

    public void draw(PointCircleMapMarkerView.PointMapMarkerData data) {

        pointMarkerView.draw(data.getLatLng());
        pointMarkerView.setText(data.getText());
        pointMarkerView.setIcon(BitmapDescriptorFactory.fromResource(data.getDrawableId()));
        showInfoWidow(0);
        createCircle(data.getLatLng(), aMap);
        startCountTime();
    }

    private void startCountTime() {
        timesubscription = Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    showInfoWidow(aLong);
                });
    }

    private void showInfoWidow(long longTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String time = TimeUtils.millis2String(longTime * 1000, sdf);
        PointCircleMapMarkerView.PointMapWindowData data = PointCircleMapMarkerView.PointMapWindowData.createTimeData(true, time);
        pointMarkerView.showInfoWindow(data);
    }

    private void stopCountTime() {
        if (null != timesubscription && !timesubscription.isUnsubscribed()) {
            timesubscription.unsubscribe();
        }

        if (null != pointMarkerView) {
            pointMarkerView.hideInfoWindow();
        }


    }


    private void createCircle(LatLng latlng, AMap aMap) {
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
        });
    }


    public void destory() {
        pointMarkerView.destory();
        removeCircle();
    }

    public void removeFromMap() {
        pointMarkerView.removeFromMap();
        removeCircle();

    }

    private void removeCircle() {
        if (null != valueAnimator) {
            valueAnimator.cancel();
        }
        if (circleList != null) {
            for (Circle circle : circleList) {
                circle.remove();
            }
            circleList.clear();
        }
    }
}
