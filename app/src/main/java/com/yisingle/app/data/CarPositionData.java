package com.yisingle.app.data;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by jikun on 17/6/14.
 */

public class CarPositionData  implements Parcelable {


    //纬度
    private double latitude;

    //经度
    private double longitude;


    //城市名称
    private String city;

    //城市code
    private String cityCode;

    //速度
    private float speed;

    //地址信息
    private String address;

    //获取方向角(单位：度） 默认值：0.0

    private float bearing;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.city);
        dest.writeString(this.cityCode);
        dest.writeFloat(this.speed);
        dest.writeString(this.address);
        dest.writeFloat(this.bearing);
    }

    public CarPositionData() {
    }

    protected CarPositionData(Parcel in) {
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.city = in.readString();
        this.cityCode = in.readString();
        this.speed = in.readFloat();
        this.address = in.readString();
        this.bearing = in.readFloat();
    }

    public static final Parcelable.Creator<CarPositionData> CREATOR = new Parcelable.Creator<CarPositionData>() {
        @Override
        public CarPositionData createFromParcel(Parcel source) {
            return new CarPositionData(source);
        }

        @Override
        public CarPositionData[] newArray(int size) {
            return new CarPositionData[size];
        }
    };

    @Override
    public String toString() {
        return "CarPositionData{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", city='" + city + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", speed=" + speed +
                ", address='" + address + '\'' +
                ", bearing=" + bearing +
                '}';
    }
}
