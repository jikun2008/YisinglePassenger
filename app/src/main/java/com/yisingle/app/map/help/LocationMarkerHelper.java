package com.yisingle.app.map.help;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.yisingle.app.R;
import com.yisingle.app.map.utils.CoordinateTransUtils;
import com.yisingle.app.map.view.MarkerBuilder;

/**
 * Created by jikun on 17/5/17.
 */


public class LocationMarkerHelper implements SensorEventHelper.OnRotationListener {
    protected Marker locMarker = null;//当前定位的地图marker

    private Context mcontext;

    private SensorEventHelper sensorEventHelper;

    public LocationMarkerHelper(Context context) {
        mcontext = context;
        initSensorEventHelper();

    }

    private void initSensorEventHelper() {
        sensorEventHelper = new SensorEventHelper(mcontext);
        sensorEventHelper.setOnRotationListener(this);
    }

    public void unInitSensorEventHelper() {
        if (null != sensorEventHelper) {
            sensorEventHelper.destroySensorEventHelper();
        }

    }

    public void destroy() {
        unInitSensorEventHelper();
        if (null != locMarker) {
            locMarker.destroy();
            locMarker = null;
        }

    }

    public Marker getLocMarker() {
        return locMarker;
    }

    public void setLocMarker(Marker locMarker) {
        this.locMarker = locMarker;
    }

    public void setLocationMarkerPosition(AMapLocation location, AMap aMap) {

        LatLng latLng = CoordinateTransUtils.changToLatLng(location);
        if (locMarker != null) {
            locMarker.setPosition(latLng);
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(mcontext.getResources(),
                    R.mipmap.navi_map_gps_locked);

            locMarker = MarkerBuilder.getAddMarkerToMapView(latLng,
                    bitmap, aMap);
        }
    }


    @Override
    public void onRotationChange(float angle) {
        if (null != locMarker) {
            locMarker.setRotateAngle(angle);
        }
    }
}
