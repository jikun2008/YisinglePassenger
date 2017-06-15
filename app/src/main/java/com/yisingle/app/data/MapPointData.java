package com.yisingle.app.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

import com.amap.api.maps.model.LatLng;
import com.yisingle.app.R;

/**
 * Created by jikun on 17/6/13.
 */

public class MapPointData implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeParcelable(this.latLng, flags);
        dest.writeInt(this.res);
    }

    protected MapPointData(Parcel in) {
        this.text = in.readString();
        this.latLng = in.readParcelable(LatLng.class.getClassLoader());
        this.res = in.readInt();
    }

    public static final Parcelable.Creator<MapPointData> CREATOR = new Parcelable.Creator<MapPointData>() {
        @Override
        public MapPointData createFromParcel(Parcel source) {
            return new MapPointData(source);
        }

        @Override
        public MapPointData[] newArray(int size) {
            return new MapPointData[size];
        }
    };
}
