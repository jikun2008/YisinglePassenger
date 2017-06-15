package com.yisingle.app.map.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.yisingle.app.R;
import com.yisingle.app.map.utils.CircleBuilder;
import com.yisingle.app.map.utils.MarkerBuilder;

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


    public void addMarkViewToMap(LatLng latLng, @DrawableRes int res, AMap aMap, boolean ishowInfoWindow) {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                res);
        currentMarker = MarkerBuilder.getStartMarkerToMapView(latLng,
                bitmap, aMap);
        initMarkInfoWindowAdapter(aMap);
        if (ishowInfoWindow) {
            currentMarker.showInfoWindow();
        }

    }


    public void addCircleViewToMap(LatLng latlng, AMap aMap) {
        circleList = new ArrayList<>();
        int radius = 30;

        for (int i = 0; i < 4; i++) {
            radius = radius + 30 * i;
            circleList.add(CircleBuilder.addCircle(latlng, radius, aMap));
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
                            R.layout.map_fast_car_info_window, null);
                    LinearLayout ll_have_net = (LinearLayout) infoWindow.findViewById(R.id.ll_have_net);

                    LinearLayout ll_no_net = (LinearLayout) infoWindow.findViewById(R.id.ll_no_net);
                    LinearLayout ll_left = (LinearLayout) infoWindow.findViewById(R.id.ll_left);
                    ImageView iv_loading = (ImageView) infoWindow.findViewById(R.id.iv_loading);
                    ll_have_net.setVisibility(View.VISIBLE);
                    ll_no_net.setVisibility(View.GONE);
                    ll_left.setVisibility(View.VISIBLE);
                    iv_loading.setVisibility(View.GONE);
                }


                return infoWindow;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
    }

}
