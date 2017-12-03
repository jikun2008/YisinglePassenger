package com.map.library.view.base;

import android.support.annotation.DrawableRes;

import com.amap.api.maps.model.LatLng;

public class BaseMarkerData {

    protected LatLng latLng;

    protected int drawableId;

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(@DrawableRes int drawableId) {
        this.drawableId = drawableId;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}