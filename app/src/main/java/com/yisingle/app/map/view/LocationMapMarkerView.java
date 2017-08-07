package com.yisingle.app.map.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.SPUtils;
import com.map.library.help.AMapLocationHelper;
import com.map.library.help.SensorEventHelper;
import com.map.library.view.base.BaseMapMarkerView;
import com.map.library.view.base.BaseMarkerData;
import com.map.library.view.base.BaseWindowData;
import com.map.library.view.utils.CoordinateTransUtils;
import com.yisingle.app.R;
import com.yisingle.app.base.Constant;
import com.yisingle.app.map.utils.MarkerBuilder;
import com.yisingle.app.map.view.LocationMapMarkerView.LocationMapMarkerData;
import com.yisingle.app.utils.BitMapUtils;


/**
 * Created by jikun on 17/5/17.
 */


public class LocationMapMarkerView extends BaseMapMarkerView<LocationMapMarkerData, BaseWindowData> {


    private SensorEventHelper sensorEventHelper;

    private AMapLocationHelper aMapLocationHelper;

    private boolean isMove = false;

    public LocationMapMarkerView(AMap aMap, Context mContext) {
        super(aMap, mContext);
    }

    @Override
    protected Marker buildMarker(LocationMapMarkerData markerData) {

        AMapLocation location = AMapLocationHelper.getLastKnownLocation(getContext());

        LatLng latLng = CoordinateTransUtils.changToLatLng(location);

        if (null == latLng) {
            latLng = new LatLng(39.908692, 116.397477);//天安门
        }
        Marker marker = initAddMarker(latLng);


        return marker;
    }

    @Override
    protected void addMarkSuccess(LocationMapMarkerData markerData) {
        initLocationHelper(markerData.isMove());

        initSensorEventHelper();
    }


    private Marker initAddMarker(LatLng latLng) {
        boolean isLoginSuccess = SPUtils.getInstance().getBoolean(Constant.IS_LOGIN_SUCCESS, false);
        Bitmap bitmap = null;
        if (isLoginSuccess) {

            bitmap = getLoginMarkIcon(R.mipmap.touxiang);
        } else {
            bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                    R.mipmap.navi_map_gps_locked);
        }


        Marker marker = MarkerBuilder.getLocationToMapView(latLng,
                bitmap, getMap());
        return marker;
    }


    private void moveToCamera(LatLng latLng) {
        float zoom = 17;//设置缩放级别
        if (null != getMap()) {
            getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));//zoom - 缩放级别，[3-20]。
        }

    }

    @Override
    public void removeView() {
        super.removeView();

        unInitLocationHelper();
        unInitSensorEventHelper();
    }


    /**
     * 改变定位Marker的坐标点
     *
     * @param location
     */
    public void changeMarkerViewPosition(AMapLocation location) {

        LatLng latLng = CoordinateTransUtils.changToLatLng(location);
        changeMarkerViewPosition(latLng);
    }

    /**
     * 改变定位Marker的坐标点
     *
     * @param latLng
     */
    public void changeMarkerViewPosition(LatLng latLng) {

        if (null != latLng && null != getMarker()) {
            getMarker().setPosition(latLng);
        }
    }


    public void startLocationToView() {
        if (null != aMapLocationHelper) {
            beginLocation(true);

        }
    }

    private void initLocationHelper(boolean isMove) {
        if (null == aMapLocationHelper) {
            aMapLocationHelper = new AMapLocationHelper(getContext());

        }

        beginLocation(isMove);
    }


    private void beginLocation(boolean isMove) {
        this.isMove = isMove;
        aMapLocationHelper.startSingleLocate(new AMapLocationHelper.OnLocationGetListeneAdapter() {
            @Override
            public void onLocationGetSuccess(AMapLocation loc) {
                LatLng latLng = CoordinateTransUtils.changToLatLng(loc);
                if (null == getMarker()) {
                    initAddMarker(latLng);
                } else {
                    changeMarkerViewPosition(latLng);

                }
                if (isMove) {
                    moveToCamera(latLng);
                }
            }
        });
    }

    private void unInitLocationHelper() {
        if (null != aMapLocationHelper) {
            aMapLocationHelper.destroyLocation();
            aMapLocationHelper = null;
        }
    }

    private void initSensorEventHelper() {
        if (null == sensorEventHelper) {
            sensorEventHelper = new SensorEventHelper(getContext());
        }
        sensorEventHelper = new SensorEventHelper(getContext());
        sensorEventHelper.setOnRotationListener(angle -> {
            if (null != getMarker()) {
                getMarker().setRotateAngle(angle);
            }
        });
    }

    private void unInitSensorEventHelper() {
        if (null != sensorEventHelper) {
            sensorEventHelper.destroySensorEventHelper();
            sensorEventHelper = null;
        }

    }

    public static class LocationMapMarkerData extends BaseMarkerData {


        private LocationMapMarkerData() {

        }

        public static LocationMapMarkerData createData(boolean isMove) {
            LocationMapMarkerData data = new LocationMapMarkerData();
            data.setMove(isMove);
            return data;
        }

        private boolean isMove;

        public boolean isMove() {
            return isMove;
        }

        public void setMove(boolean move) {
            isMove = move;
        }

    }

    public void setMarkIcon(@DrawableRes int drawableId) {
        if (getMarker() != null) {

            Bitmap bitmap = getLoginMarkIcon(drawableId);
            getMarker().setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
        }

    }

    private Bitmap getLoginMarkIcon(@DrawableRes int drawableId) {

        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                R.mipmap.navi_map_gps_locked);
        Bitmap rouudBitmap = BitMapUtils.getRoundBitMap(getContext().getResources(), drawableId);


        int newBitmtWith = bitmap.getHeight() / 4;
        Bitmap newBitmap = ImageUtils.scale(rouudBitmap, newBitmtWith, newBitmtWith);
        Bitmap combieBitmap = BitMapUtils.combineBitmap(bitmap, newBitmap);
        return combieBitmap;
    }
}
