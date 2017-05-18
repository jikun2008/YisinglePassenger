package com.yisingle.app.map.utils;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;

/**
 * Created by jikun on 17/5/12.
 */

public class CoordinateTransUtils {

    public static LatLng changToLatLng(AMapLocation loc) {
        return new LatLng(loc.getLatitude(), loc.getLongitude());
    }
}
