package com.yisingle.app.map.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.yisingle.app.utils.GLFont;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jikun on 17/5/12.
 */

@SuppressWarnings("unused")
public class MarkerBuilder {
    //    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
//    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    //224,236,237
    //233,241,242
    private static final String LOCATION_MARKER_FLAG = "mylocation";
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

    private static final float zIndex = 5f;

    public static Circle getAddCircleToMapView(LatLng latlng, double radius, AMap aMap) {
        CircleOptions options = new CircleOptions();
        options.strokeWidth(1f);
        options.fillColor(FILL_COLOR);
        options.strokeColor(STROKE_COLOR);
        options.zIndex(zIndex);
        options.center(latlng);
        options.radius(radius);
        return aMap.addCircle(options);
    }


    public static Marker getTextToMapView(String text, LatLng latlng, AMap aMap, int size) {


        Bitmap bitmap = GLFont.getImage(text, size);
        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        options.anchor(0f, 0f);
        options.zIndex(zIndex);
        options.position(latlng);
        Marker mLocMarker = aMap.addMarker(options);
        mLocMarker.setTitle("text");
        return mLocMarker;
    }


    public static Marker getLocationToMapView(LatLng latlng, Bitmap bitmap, AMap aMap) {

        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        options.zIndex(zIndex - 1);
        Marker mLocMarker = aMap.addMarker(options);
        mLocMarker.setTitle(LOCATION_MARKER_FLAG);
        return mLocMarker;
    }


    public static Marker getCarMarkerToMapView(LatLng latlng, Bitmap bitmap, AMap aMap, float rotateAngle) {

        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        options.zIndex(zIndex - 2);
        Marker mLocMarker = aMap.addMarker(options);
        mLocMarker.setRotateAngle(rotateAngle);
        mLocMarker.setTitle(LOCATION_MARKER_FLAG);
        return mLocMarker;
    }

    public static Marker getAddMarkerToMapView(LatLng latlng, Bitmap bitmap, AMap aMap) {

        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        options.zIndex(zIndex);
        Marker mLocMarker = aMap.addMarker(options);
        mLocMarker.setTitle(LOCATION_MARKER_FLAG);
        return mLocMarker;
    }

    public static Marker getStartMarkerToMapView(LatLng latlng, Bitmap bitmap, AMap aMap) {
        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        options.anchor(0.5f, 1);
        options.position(latlng);
        options.zIndex(zIndex);
        Marker marker = aMap.addMarker(options);
        marker.setTitle("start");
        return marker;
    }

    public static Marker getCenterMarkerToMapView(BitmapDescriptor bitmapDescriptor, AMap aMap) {
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        MarkerOptions options = new MarkerOptions();
        options.icon(bitmapDescriptor);
        options.anchor(0.5f, 1.0f);
        options.zIndex(zIndex);
        Marker marker = aMap.addMarker(options);
        marker.setTitle("CenterMarker");////如果要显示InfoWindow(无论是否自定义)一定要设置Title否则无论你怎么设置都不会显示infowindow
        //设置Marker在屏幕上,不跟随地图移动
        marker.setPositionByPixels(screenPosition.x, screenPosition.y);
        return marker;
    }

    public static Marker getCenterMarkerToMapView(List<Bitmap> bitmapList, AMap aMap) {

        ArrayList<BitmapDescriptor> list = new ArrayList<>();
        for (Bitmap bimap : bitmapList) {
            list.add(BitmapDescriptorFactory.fromBitmap(bimap));
        }
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        MarkerOptions options = new MarkerOptions();
        options.icons(list);
        options.zIndex(zIndex);

        options.anchor(0.5f, 1.0f);
        Marker marker = aMap.addMarker(options);
        //设置Marker在屏幕上,不跟随地图移动
        marker.setPositionByPixels(screenPosition.x, screenPosition.y);
        marker.setPeriod(5);// 设置多少帧刷新一次图片资源，Marker动画的间隔时间，值越小动画越快。默认为20，最小为1。

        return marker;
    }


}
