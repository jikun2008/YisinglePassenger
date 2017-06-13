package com.yisingle.app.map.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.yisingle.app.R;
import com.yisingle.app.map.help.AMapLocationHelper;
import com.yisingle.app.map.help.SensorEventHelper;
import com.yisingle.app.map.utils.CoordinateTransUtils;


/**
 * Created by jikun on 17/5/17.
 */


public class LocationMapMarkerView extends BaseMapMarkerView implements SensorEventHelper.OnRotationListener {


    private SensorEventHelper sensorEventHelper;

    private AMapLocationHelper aMapLocationHelper;

    public LocationMapMarkerView(Context mContext) {
        super(mContext);

    }


    public void addMarkViewToMap(AMap aMap, boolean isMove) {
        initSensorEventHelper();
        aMapLocationHelper = new AMapLocationHelper(mContext);

        aMapLocationHelper.startSingleLocate(new AMapLocationHelper.OnLocationGetListeneAdapter() {
            @Override
            public void onLocationGetSuccess(AMapLocation loc) {
                LatLng latLng = CoordinateTransUtils.changToLatLng(loc);
                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                        R.mipmap.navi_map_gps_locked);

                currentMarker = MarkerBuilder.getAddMarkerToMapView(latLng,
                        bitmap, aMap);
                if (isMove) {
                    moveToCamera(aMap, latLng);
                }

            }
        });

    }

    public void moveToCamera(AMap aMap, LatLng latLng) {
        float zoom = 16;//设置缩放级别
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));//zoom - 缩放级别，[3-20]。
    }

    @Override
    public void removeMarkerViewFromMap() {
        super.removeMarkerViewFromMap();
        if (null != aMapLocationHelper) {
            aMapLocationHelper.destroyLocation();
        }

        unInitSensorEventHelper();
    }

    public Marker getLocMarker() {
        return currentMarker;
    }


    public void setMarkerViewPosition(AMapLocation location) {

        LatLng latLng = CoordinateTransUtils.changToLatLng(location);
        if (currentMarker != null) {
            currentMarker.setPosition(latLng);
        }
    }


    @Override
    public void onRotationChange(float angle) {
        if (null != currentMarker) {
            currentMarker.setRotateAngle(angle);
        }
    }


    private void initSensorEventHelper() {
        sensorEventHelper = new SensorEventHelper(mContext);
        sensorEventHelper.setOnRotationListener(this);
    }

    private void unInitSensorEventHelper() {
        if (null != sensorEventHelper) {
            sensorEventHelper.destroySensorEventHelper();
        }

    }
}
