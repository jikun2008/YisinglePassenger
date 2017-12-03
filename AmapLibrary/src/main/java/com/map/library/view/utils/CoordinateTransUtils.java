package com.map.library.view.utils;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;

/**
 * Created by jikun on 17/5/12.
 */

public class CoordinateTransUtils {

    public static LatLng changToLatLng(AMapLocation location) {
        if (location != null) {
            return new LatLng(location.getLatitude(), location.getLongitude());
        } else {
            return null;
        }

    }
}
