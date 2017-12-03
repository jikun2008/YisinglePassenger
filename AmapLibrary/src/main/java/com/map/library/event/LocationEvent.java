package com.map.library.event;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import com.amap.api.location.AMapLocation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jikun on 17/5/12.
 * EventBus传递参数
 */

@SuppressWarnings("unused")
public class LocationEvent implements Parcelable {


    private AMapLocation mapLocation;


    private

    int code;


    public AMapLocation getMapLocation() {
        return mapLocation;
    }

    public void setMapLocation(AMapLocation mapLocation) {
        this.mapLocation = mapLocation;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public LocationEvent(@Code int code, AMapLocation mapLocation) {
        this.code = code;
        this.mapLocation = mapLocation;
    }

    //添加支持注解的依赖到你的项目中，需要在build.gradle文件中的依赖块中添加：
    //dependencies { compile 'com.android.support:support-annotations:24.2.0' }
    @IntDef({Code.SUCCESS, Code.FAILED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Code {

        int SUCCESS = 0;
        int FAILED = 1;


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mapLocation, flags);
        dest.writeInt(this.code);
    }

    protected LocationEvent(Parcel in) {
        this.mapLocation = in.readParcelable(AMapLocation.class.getClassLoader());
        this.code = in.readInt();
    }

    public static final Creator<LocationEvent> CREATOR = new Creator<LocationEvent>() {
        @Override
        public LocationEvent createFromParcel(Parcel source) {
            return new LocationEvent(source);
        }

        @Override
        public LocationEvent[] newArray(int size) {
            return new LocationEvent[size];
        }
    };
}
