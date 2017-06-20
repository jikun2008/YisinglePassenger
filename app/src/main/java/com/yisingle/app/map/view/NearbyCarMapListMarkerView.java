package com.yisingle.app.map.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.yisingle.app.R;
import com.yisingle.app.data.CarPositionData;
import com.yisingle.app.map.utils.MarkerBuilder;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by jikun on 17/6/14.
 */

public class NearbyCarMapListMarkerView extends BaseMapListMarkerView<NearbyCarMapListMarkerView.NearbyCarMapMarkerData, BaseWindowData> {


    public NearbyCarMapListMarkerView(AMap aMap, Context context) {
        super(aMap, context);

    }

    @Override
    protected List<BaseMapMarkerView<NearbyCarMapMarkerData, BaseWindowData>> getListMarker(List<NearbyCarMapMarkerData> markerDataList) {

        List<BaseMapMarkerView<NearbyCarMapMarkerData, BaseWindowData>> list = new ArrayList<>();
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                R.mipmap.car);

        for (NearbyCarMapMarkerData nearbyCarMapMarkerData : markerDataList) {


            BaseMapMarkerView<NearbyCarMapMarkerData, BaseWindowData> baseMapMarkerView = new BaseMapMarkerView<NearbyCarMapMarkerData, BaseWindowData>(getMap(), getContext()) {
                @Override
                protected Marker buildMarker(NearbyCarMapMarkerData data) {

                    Marker marker = MarkerBuilder.getCarMarkerToMapView(data.getLatLng(), bitmap, getMap(), data.getBearing());
                    return marker;
                }
            };
            baseMapMarkerView.addView(nearbyCarMapMarkerData);
            list.add(baseMapMarkerView);
        }


        return list;
    }

    public static class NearbyCarMapMarkerData extends BaseMarkerData {
        //获取方向角(单位：度） 默认值：0.0

        private float bearing;

        public float getBearing() {
            return bearing;
        }

        public void setBearing(float bearing) {
            this.bearing = bearing;
        }

        public static NearbyCarMapMarkerData createData(LatLng latLng, float bearing) {
            NearbyCarMapMarkerData data = new NearbyCarMapMarkerData();
            data.setLatLng(latLng);
            data.setBearing(bearing);
            return data;
        }
    }


    public static List<NearbyCarMapMarkerData> changeList(List<CarPositionData> list) {
        List<NearbyCarMapMarkerData> dataList = new ArrayList<>();
        for (CarPositionData carPositionData : list) {

            LatLng latLng = new LatLng(carPositionData.getLatitude(), carPositionData.getLongitude());
            dataList.add(NearbyCarMapMarkerData.createData(latLng, carPositionData.getBearing()));
        }
        return dataList;
    }


}

