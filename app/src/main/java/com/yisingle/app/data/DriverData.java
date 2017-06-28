package com.yisingle.app.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jikun on 17/6/28.
 */

public class DriverData implements Parcelable {


    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    private String driverName;

    private String password;//密码

    private String latitude;//纬度

    private String longitude;//经度


    private String deviceId;//设备id


    private String phonenum;//电话号码
    @DriverState
    private int state;

    //添加支持注解的依赖到你的项目中，需要在build.gradle文件中的依赖块中添加：
    //dependencies { compile 'com.android.support:support-annotations:24.2.0' }
    @IntDef({DriverState.WATI_FOR_ORDER, DriverState.SERVICE, DriverState.BREAKDOWN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DriverState {


        int WATI_FOR_ORDER = 0;
        int SERVICE = 1;
        int BREAKDOWN = 2;

    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public
    @DriverState
    int getState() {
        return state;
    }

    public void setState(@DriverState int state) {
        this.state = state;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.driverName);
        dest.writeString(this.password);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.deviceId);
        dest.writeString(this.phonenum);
        dest.writeInt(this.state);
    }

    public DriverData() {
    }

    protected DriverData(Parcel in) {
        this.id = in.readInt();
        this.driverName = in.readString();
        this.password = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.deviceId = in.readString();
        this.phonenum = in.readString();
        this.state = in.readInt();
    }

    public static final Parcelable.Creator<DriverData> CREATOR = new Parcelable.Creator<DriverData>() {
        @Override
        public DriverData createFromParcel(Parcel source) {
            return new DriverData(source);
        }

        @Override
        public DriverData[] newArray(int size) {
            return new DriverData[size];
        }
    };
}
