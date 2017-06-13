package com.yisingle.app.map.view;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;

/**
 * Created by jikun on 17/5/19.
 */

public abstract class BaseMapMarkerView {


    protected Marker currentMarker = null;

    protected Context mContext;


    public BaseMapMarkerView(Context mContext) {
        this.mContext = mContext;
    }

    public boolean isAddMarkViewToMap() {
        return currentMarker != null;
    }


    public void removeMarkerViewFromMap() {
        if (null != currentMarker) {
            currentMarker.destroy();
            currentMarker = null;
        }
    }

}
