package com.yisingle.app.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jikun on 17/6/27.
 */

public class SendOrderData implements Parcelable {


    @State
    private int state;
    private int id;

    private String phoneNum;


    private String startLatitude;


    private String startLongitude;


    private String startPlaceName;


    private String endLatitude;


    private String endLongitude;


    private String endPlaceName;

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
        dest.writeString(this.startPlaceName);
        dest.writeString(this.endLatitude);
        dest.writeString(this.endLongitude);
        dest.writeString(this.endPlaceName);
    }

    public SendOrderData() {
    }

    protected SendOrderData(Parcel in) {
        this.id = in.readInt();
        this.phoneNum = in.readString();
        this.startLatitude = in.readString();
        this.startLongitude = in.readString();
        this.startPlaceName = in.readString();
        this.endLatitude = in.readString();
        this.endLongitude = in.readString();
        this.endPlaceName = in.readString();
    }

    public static final Parcelable.Creator<SendOrderData> CREATOR = new Parcelable.Creator<SendOrderData>() {
        @Override
        public SendOrderData createFromParcel(Parcel source) {
            return new SendOrderData(source);
        }

        @Override
        public SendOrderData[] newArray(int size) {
            return new SendOrderData[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public
    @State
    int getState() {
        return state;
    }

    public void setState(@State int state) {
        this.state = state;
    }

    //添加支持注解的依赖到你的项目中，需要在build.gradle文件中的依赖块中添加：
    //dependencies { compile 'com.android.support:support-annotations:24.2.0' }
    @IntDef({State.NEW, State.OLDER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {

        int NEW = 66;
        int OLDER = 67;

    }
}
