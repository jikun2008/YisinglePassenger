package com.yisingle.app.map.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

import com.yisingle.app.R;
import com.yisingle.app.map.help.SensorEventHelper;
import com.yisingle.app.map.utils.CoordinateTransUtils;


/**
 * Created by jikun on 17/5/17.
 */


public class LocationMapMarkerView extends BaseMapMarkerView implements SensorEventHelper.OnRotationListener {
    protected Marker locMarker = null;//当前定位的地图marker

    private Context mcontext;

    private SensorEventHelper sensorEventHelper;

    public LocationMapMarkerView(Context context) {
        mcontext = context;
        initSensorEventHelper();

    }


    public void addMarkerViewToMap(AMapLocation location, AMap aMap) {
        LatLng latLng = CoordinateTransUtils.changToLatLng(location);
        Bitmap bitmap = BitmapFactory.decodeResource(mcontext.getResources(),
                R.mipmap.navi_map_gps_locked);

        locMarker = MarkerBuilder.getAddMarkerToMapView(latLng,
                bitmap, aMap);

    }


    public void removeMarkerViewFromMap() {
        unInitSensorEventHelper();
        if (null != locMarker) {
            locMarker.destroy();
            locMarker = null;
        }

    }

    public Marker getLocMarker() {
        return locMarker;
    }


    public void setMarkerViewPosition(AMapLocation location) {

        LatLng latLng = CoordinateTransUtils.changToLatLng(location);
        if (locMarker != null) {
            locMarker.setPosition(latLng);
        }
    }

    @Override
    public boolean isAddMarkViewToMap() {
        return locMarker != null;
    }


    @Override
    public void onRotationChange(float angle) {
        if (null != locMarker) {
            locMarker.setRotateAngle(angle);
        }
    }


    private void initSensorEventHelper() {
        sensorEventHelper = new SensorEventHelper(mcontext);
        sensorEventHelper.setOnRotationListener(this);
    }

    private void unInitSensorEventHelper() {
        if (null != sensorEventHelper) {
            sensorEventHelper.destroySensorEventHelper();
        }

    }
}
