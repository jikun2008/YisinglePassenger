package com.yisingle.app.data;

/**
 * Created by jikun on 17/7/21.
 */
public class HeartBeatData {
    private int code = 0;

    private int id;


    private String latitude;//纬度

    private String longitude;//经度

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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "HeartBeatData{" +
                "code=" + code +
                ", id=" + id +
                '}';
    }
}
