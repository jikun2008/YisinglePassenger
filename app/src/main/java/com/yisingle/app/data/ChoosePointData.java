package com.yisingle.app.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.map.library.view.utils.RegeocodeAddressInfoUtils;

import java.util.List;

/**
 * Created by jikun on 17/6/14.
 */

public class ChoosePointData implements Parcelable {


    private LatLng latLng;
    private String simpleAddress;
    private RegeocodeAddress regeocodeAddress;

    private List<CarPositionData> carPositionDatas;

    private ChoosePointData(LatLng latLng, String simpleAddress, RegeocodeAddress regeocodeAddress, List<CarPositionData> carPositionDatas) {
        this.latLng = latLng;
        this.simpleAddress = simpleAddress;
        this.regeocodeAddress = regeocodeAddress;
        this.carPositionDatas = carPositionDatas;
    }

    public static ChoosePointData createChoosePointData(LatLng latLng, RegeocodeAddress regeocodeAddress, List<CarPositionData> carPositionDatas) {

        String address = RegeocodeAddressInfoUtils.getSimpleSitename(regeocodeAddress);
        String info = RegeocodeAddressInfoUtils.getRegeocodeAddress(regeocodeAddress);
        Log.e("测试代码", "测试代码" + info);
        ChoosePointData choosePointData = new ChoosePointData(latLng, address, regeocodeAddress, carPositionDatas);
        return choosePointData;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getSimpleAddress() {
        return simpleAddress;
    }

    public void setSimpleAddress(String simpleAddress) {
        this.simpleAddress = simpleAddress;
    }

    public RegeocodeAddress getRegeocodeAddress() {
        return regeocodeAddress;
    }

    public void setRegeocodeAddress(RegeocodeAddress regeocodeAddress) {
        this.regeocodeAddress = regeocodeAddress;
    }

    public List<CarPositionData> getCarPositionDatas() {
        return carPositionDatas;
    }

    public void setCarPositionDatas(List<CarPositionData> carPositionDatas) {
        this.carPositionDatas = carPositionDatas;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.latLng, flags);
        dest.writeString(this.simpleAddress);
        dest.writeParcelable(this.regeocodeAddress, flags);
        dest.writeTypedList(this.carPositionDatas);
    }

    protected ChoosePointData(Parcel in) {
        this.latLng = in.readParcelable(LatLng.class.getClassLoader());
        this.simpleAddress = in.readString();
        this.regeocodeAddress = in.readParcelable(RegeocodeAddress.class.getClassLoader());
        this.carPositionDatas = in.createTypedArrayList(CarPositionData.CREATOR);
    }

    public static final Parcelable.Creator<ChoosePointData> CREATOR = new Parcelable.Creator<ChoosePointData>() {
        @Override
        public ChoosePointData createFromParcel(Parcel source) {
            return new ChoosePointData(source);
        }

        @Override
        public ChoosePointData[] newArray(int size) {
            return new ChoosePointData[size];
        }
    };
}
