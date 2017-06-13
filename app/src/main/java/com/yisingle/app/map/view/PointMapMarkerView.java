package com.yisingle.app.map.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

/**
 * Created by jikun on 17/6/11.
 */

public class PointMapMarkerView extends BaseMapMarkerView {

    protected Marker textMarker = null;

    private int size;

    private String text;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public PointMapMarkerView(Context mContext, int size) {
        super(mContext);
        this.size = size;
    }

    @Override
    public boolean isAddMarkViewToMap() {
        return currentMarker != null;
    }


    public void addMarkViewToMap(String text, LatLng latLng, @DrawableRes int res, AMap aMap) {


        this.text = text;
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                res);
        currentMarker = MarkerBuilder.getStartMarkerToMapView(latLng,
                bitmap, aMap);

        textMarker = MarkerBuilder.getTextToMapView(text, latLng, aMap, size);

    }

    @Override
    public void removeMarkerViewFromMap() {
        super.removeMarkerViewFromMap();
        if (null != textMarker) {
            textMarker.destroy();
            textMarker = null;
        }

    }

}
