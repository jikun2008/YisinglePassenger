package com.yisingle.app.map.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.yisingle.app.R;
import com.yisingle.app.data.CarPositionData;
import com.yisingle.app.data.ChoosePointData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jikun on 17/6/14.
 */

public class NearbyCarMapMarkerView extends BaseMapMarkerView {

    private List<Marker> markerList;

    public NearbyCarMapMarkerView(Context mContext) {
        super(mContext);
        markerList = new ArrayList<>();
    }

    public void addMarkerViewToMap(AMap aMap, ChoosePointData datas) {

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                R.mipmap.car);

        for (CarPositionData carPositionData : datas.getCarPositionDatas()) {

            LatLng latLng = new LatLng(carPositionData.getLatitude(), carPositionData.getLongitude());
            Marker marker = MarkerBuilder.getCarMarkerToMapView(latLng, bitmap, aMap, carPositionData.getBearing());
            markerList.add(marker);
        }


    }


    @Override
    public void removeMarkerViewFromMap() {
        super.removeMarkerViewFromMap();
        if (null != markerList) {
            for (Marker marker : markerList) {
                if (null != marker) {
                    marker.destroy();
                }

            }
            markerList.clear();
        }
    }
}
