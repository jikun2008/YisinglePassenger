package com.yisingle.app.map.view;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

/**
 * Created by jikun on 17/5/12.
 */

public class MarkerBuilder {

    public static final String LOCATION_MARKER_FLAG = "mylocation";
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

    public static Circle getAddCircleToMapView(LatLng latlng, double radius, AMap aMap) {
        CircleOptions options = new CircleOptions();
        options.strokeWidth(1f);
        options.fillColor(FILL_COLOR);
        options.strokeColor(STROKE_COLOR);
        options.center(latlng);
        options.radius(radius);
        Circle circle = aMap.addCircle(options);
        return circle;
    }


    public static Marker getAddMarkerToMapView(LatLng latlng, Bitmap bitmap, AMap aMap) {

        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        options.anchor(0.5f, 0.5f);
        options.position(latlng);
        Marker mLocMarker = aMap.addMarker(options);
        mLocMarker.setTitle(LOCATION_MARKER_FLAG);
        return mLocMarker;
    }

    public static Marker getCenterMarkerToMapView(Bitmap bitmap, AMap aMap) {
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        Marker marker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));
        //设置Marker在屏幕上,不跟随地图移动
        marker.setPositionByPixels(screenPosition.x, screenPosition.y);
        marker.setZIndex(1);
        return marker;
    }
}
