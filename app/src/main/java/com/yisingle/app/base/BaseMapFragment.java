package com.yisingle.app.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.yisingle.app.R;
import com.yisingle.app.map.help.SensorEventHelper;
import com.yisingle.app.map.utils.CoordinateTransUtils;
import com.yisingle.app.map.view.MarkerBuilder;

import java.lang.reflect.Field;

/**
 * Created by jikun on 17/5/10.
 */

public abstract class BaseMapFragment extends BaseFrament implements SensorEventHelper.OnRotationListener {
    private TextureMapView mapView;
    protected AMap aMap;
    protected Marker locMarker = null;//当前定位的地图marker
    protected Circle locCircle = null;//当前定位的地图Circle
    protected Marker centerMarker = null;

    private SensorEventHelper sensorEventHelper;

    protected abstract TextureMapView getTextureMapView();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (isOpenSensorEventHelper()) {
            initSensorEventHelper();
        }

        mapView = getTextureMapView();


        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
            aMap = mapView.getMap();
            aMap.setOnMapLoadedListener(() -> initMapLoad());
        }

    }


    /**
     * 初始化地图一些initMapCreate 在onActivityCreated后调用
     */
    public abstract void initMapLoad();

    public abstract boolean isOpenSensorEventHelper();


    public void initSensorEventHelper() {
        sensorEventHelper = new SensorEventHelper(getContext());
        sensorEventHelper.setOnRotationListener(this);
    }

    public void unInitSensorEventHelper() {
        if (null != sensorEventHelper) {
            sensorEventHelper.destroySensorEventHelper();
        }

    }

    /**
     * 方法必须重写
     */
    @Override

    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != locMarker) {
            locMarker.destroy();
        }
        if (null != centerMarker) {
            centerMarker.destroy();
        }
        locMarker = null;
        centerMarker = null;
        locCircle = null;
        mapView.onDestroy();
        unInitSensorEventHelper();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected void addCurrentMarkToMap(AMapLocation location) {
        LatLng latLng = CoordinateTransUtils.changToLatLng(location);


        if (locMarker != null) {
            locMarker.setPosition(latLng);
        } else {

            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                    R.mipmap.navi_map_gps_locked);

            locMarker = MarkerBuilder.getAddMarkerToMapView(latLng,
                    bitmap, aMap);
        }
    }

    protected void addCenterMarkToMap() {
        if (centerMarker == null) {
            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                    R.mipmap.icon_me_location);
            centerMarker = MarkerBuilder.getCenterMarkerToMapView(bitmap, aMap);
        }
    }
}
