package com.yisingle.app.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jikun on 17/6/27.
 */

public class OrderData implements Parcelable {

    public DriverData getDriver() {
        return driver;
    }

    public void setDriver(DriverData driver) {
        this.driver = driver;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    private int id;


    private String phoneNum;

    private String startLatitude;


    private String startLongitude;


    private String endLatitude;

    private String endLongitude;


    private String startPlaceName;


    private String endPlaceName;


    private int orderState;


    private DriverData driver;

    private UserData user;


    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(String startLatitude) {
        this.startLatitude = startLatitude;
    }

    public String getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(String startLongitude) {
        this.startLongitude = startLongitude;
    }

    public String getStartPlaceName() {
        return startPlaceName;
    }

    public void setStartPlaceName(String startPlaceName) {
        this.startPlaceName = startPlaceName;
    }

    public String getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(String endLatitude) {
        this.endLatitude = endLatitude;
    }

    public String getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(String endLongitude) {
        this.endLongitude = endLongitude;
    }

    public String getEndPlaceName() {
        return endPlaceName;
    }

    public void setEndPlaceName(String endPlaceName) {
        this.endPlaceName = endPlaceName;
    }

    public OrderData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    @State
    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(@State int orderState) {
        this.orderState = orderState;
    }

    //添加支持注解的依赖到你的项目中，需要在build.gradle文件中的依赖块中添加：
    //dependencies { compile 'com.android.support:support-annotations:24.2.0' }
    @IntDef({State.WAIT_NEW, State.WAIT_OLD, State.HAVE_TAKE, State.HAVE_COMPLETE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {

        int WAIT_NEW = -1;
        int WAIT_OLD = 0;
        int HAVE_TAKE = 1;
        int HAVE_COMPLETE = 2;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.phoneNum);
        dest.writeString(this.startLatitude);
        dest.writeString(this.startLongitude);
        dest.writeString(this.endLatitude);
        dest.writeString(this.endLongitude);
        dest.writeString(this.startPlaceName);
        dest.writeString(this.endPlaceName);
        dest.writeInt(this.orderState);
        dest.writeParcelable(this.driver, flags);
        dest.writeParcelable(this.user, flags);
    }

    protected OrderData(Parcel in) {
        this.id = in.readInt();
        this.phoneNum = in.readString();
        this.startLatitude = in.readString();
        this.startLongitude = in.readString();
        this.endLatitude = in.readString();
        this.endLongitude = in.readString();
        this.startPlaceName = in.readString();
        this.endPlaceName = in.readString();
        this.orderState = in.readInt();
        this.driver = in.readParcelable(DriverData.class.getClassLoader());
        this.user = in.readParcelable(UserData.class.getClassLoader());
    }

    public static final Creator<OrderData> CREATOR = new Creator<OrderData>() {
        @Override
        public OrderData createFromParcel(Parcel source) {
            return new OrderData(source);
        }

        @Override
        public OrderData[] newArray(int size) {
            return new OrderData[size];
        }
    };
}
