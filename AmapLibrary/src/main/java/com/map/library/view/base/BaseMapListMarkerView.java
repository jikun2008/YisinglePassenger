package com.map.library.view.base;

import android.content.Context;

import com.amap.api.maps.AMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jikun on 17/5/19.
 */

public abstract class BaseMapListMarkerView<M extends BaseMarkerData, T extends BaseWindowData> {
    private AMap map;

    private Context context;

    private List<M> markerDataList;

    private List<T> infoDataList;

    private List<BaseMapMarkerView<M, T>> markerViewList = new ArrayList<>();

    public BaseMapListMarkerView(AMap map, Context context) {
        this.map = map;
        this.context = context;
    }

    protected abstract List<BaseMapMarkerView<M, T>> getListMarker(List<M> markerDataList);

    public void addView(List<M> markerDataList) {
        removeView();
        this.markerDataList = markerDataList;
        markerViewList = getListMarker(markerDataList);

    }

    public void removeView() {
        if (null != markerViewList) {
            for (BaseMapMarkerView baseMapMarkerView : markerViewList) {
                if (null != baseMapMarkerView) {
                    baseMapMarkerView.removeView();
                }

            }
            markerViewList.clear();

        }


    }


    public AMap getMap() {
        return map;
    }

    public void setMap(AMap map) {
        this.map = map;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public List<BaseMapMarkerView<M, T>> getMarkerViewList() {
        return markerViewList;
    }

    public void setMarkerViewList(List<BaseMapMarkerView<M, T>> markerViewList) {
        this.markerViewList = markerViewList;
    }

    public List<M> getMarkerDataList() {
        return markerDataList;
    }

    public void setMarkerDataList(List<M> markerDataList) {
        this.markerDataList = markerDataList;
    }

    public List<T> getInfoDataList() {
        return infoDataList;
    }

    public void setInfoDataList(List<T> infoDataList) {
        this.infoDataList = infoDataList;
    }
}


