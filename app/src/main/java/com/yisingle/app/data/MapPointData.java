package com.yisingle.app.data;

import android.support.annotation.DrawableRes;

import com.amap.api.maps.model.LatLng;
import com.yisingle.app.R;

/**
 * Created by jikun on 17/6/13.
 */

public class MapPointData {

    private String text;
    private LatLng latLng;
    private
    @DrawableRes
    int res;

    public MapPointData(String text, LatLng latLng, int res) {
        this.text = text;
        this.latLng = latLng;
        this.res = res;
    }


    /**
     * 建立起点Marker数据
     *
     * @return
     */
    public static MapPointData createStartMapPointData(String text, LatLng latLng) {
        MapPointData mapPointData = new MapPointData(text, latLng, R.mipmap.icon_start);
        return mapPointData;
    }

    /**
     * 建立终点Marker数据
     *
     * @return
     */
    public static MapPointData createEndMapPointData(String text, LatLng latLng) {

        MapPointData mapPointData = new MapPointData(text, latLng, R.mipmap.icon_end);
        return mapPointData;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }
}
