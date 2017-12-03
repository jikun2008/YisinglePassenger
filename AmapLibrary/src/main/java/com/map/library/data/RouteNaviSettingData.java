package com.map.library.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import com.amap.api.services.core.LatLonPoint;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class RouteNaviSettingData implements Parcelable {


    private LatLonPoint targetLatLng;
    private LatLonPoint startLatLng;
    private LatLonPoint endLatLng;
    private int type = TYPE.DO_NO_THING;

    private RouteNaviSettingData(LatLonPoint targetLatLng, LatLonPoint startLatLng, LatLonPoint endLatLng, int type) {
        this.targetLatLng = targetLatLng;
        this.startLatLng = startLatLng;
        this.endLatLng = endLatLng;
        this.type = type;
    }

    //添加支持注解的依赖到你的项目中，需要在build.gradle文件中的依赖块中添加：
    //dependencies { compile 'com.android.support:support-annotations:24.2.0' }
    @IntDef({TYPE.DO_NO_THING, TYPE.DO_ONE_START_TO_END_ROUTE,  TYPE.DO_ONE_CAR_TO_START_ROUTE,TYPE.DO_TWO_ROUTE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TYPE {

        int DO_NO_THING = 0;
        int DO_ONE_START_TO_END_ROUTE = 1;
        int DO_ONE_CAR_TO_START_ROUTE = 2;
        int DO_TWO_ROUTE = 3;


    }

    public LatLonPoint getTargetLatLng() {
        return targetLatLng;
    }

    public void setTargetLatLng(LatLonPoint targetLatLng) {
        this.targetLatLng = targetLatLng;
    }

    public LatLonPoint getStartLatLng() {
        return startLatLng;
    }

    public void setStartLatLng(LatLonPoint startLatLng) {
        this.startLatLng = startLatLng;
    }

    public LatLonPoint getEndLatLng() {
        return endLatLng;
    }

    public void setEndLatLng(LatLonPoint endLatLng) {
        this.endLatLng = endLatLng;
    }

    public
    @TYPE
    int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.targetLatLng, flags);
        dest.writeParcelable(this.startLatLng, flags);
        dest.writeParcelable(this.endLatLng, flags);
        dest.writeInt(this.type);
    }

    public RouteNaviSettingData() {
    }

    protected RouteNaviSettingData(Parcel in) {
        this.targetLatLng = in.readParcelable(LatLonPoint.class.getClassLoader());
        this.startLatLng = in.readParcelable(LatLonPoint.class.getClassLoader());
        this.endLatLng = in.readParcelable(LatLonPoint.class.getClassLoader());
        this.type = in.readInt();
    }

    public static final Creator<RouteNaviSettingData> CREATOR = new Creator<RouteNaviSettingData>() {
        @Override
        public RouteNaviSettingData createFromParcel(Parcel source) {
            return new RouteNaviSettingData(source);
        }

        @Override
        public RouteNaviSettingData[] newArray(int size) {
            return new RouteNaviSettingData[size];
        }
    };


    public static RouteNaviSettingData createOneCarToStartRouteData(LatLonPoint startLatLng) {

        RouteNaviSettingData data = new RouteNaviSettingData(null, startLatLng, null, TYPE.DO_ONE_CAR_TO_START_ROUTE);

        return data;
    }


    public static RouteNaviSettingData createOneStartToEndRouteData(LatLonPoint startLatLng,LatLonPoint endLatLng) {

        RouteNaviSettingData data = new RouteNaviSettingData(null, startLatLng, endLatLng, TYPE.DO_ONE_START_TO_END_ROUTE);

        return data;
    }

    public static RouteNaviSettingData createTwoRouteData(LatLonPoint targetLatLng, LatLonPoint startLatLng, LatLonPoint endLatLng) {
        RouteNaviSettingData data = new RouteNaviSettingData(targetLatLng, startLatLng, endLatLng, TYPE.DO_TWO_ROUTE);

        return data;
    }
}