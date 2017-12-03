package com.map.library.view;

import android.graphics.Bitmap;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

/**
 * Created by jikun on 17/5/12.
 */

@SuppressWarnings("unused")
public class MarkerBuilder {


    private static final float zIndex = 5f;


    public static Marker getMarkerToMapView(LatLng latlng, Bitmap bitmap, AMap aMap) {
        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        options.anchor(0.5f, 1);
        options.position(latlng);
        options.zIndex(zIndex);
        Marker marker = aMap.addMarker(options);
        marker.setTitle("marker");//必须设置Title 否则infoWindow显示不出来
        return marker;
    }


}
