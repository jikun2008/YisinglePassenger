package com.map.library.view.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.yisingle.baselibray.baseadapter.viewholder.MapInfoWindowViewHolder;


/**
 * Created by jikun on 17/5/19.
 */

public abstract class BaseMapMarkerView<M extends BaseMarkerData, T extends BaseWindowData> {

    private AMap map;

    private Context context;

    private Marker marker = null;

    private View infoWindow = null;

    private MapInfoWindowViewHolder viewHolder = null;


    private InfoWindowListener infoWindowListener;


    private M markerData;//Marker数据


    private T infoData;


    public M getMarkerData() {
        return markerData;
    }

    public void setMarkerData(M markerData) {
        this.markerData = markerData;
    }

    public T getInfoData() {
        return infoData;
    }

    public void setInfoData(T infoData) {
        this.infoData = infoData;
    }

    public BaseMapMarkerView(AMap aMap, Context context) {
        this.map = aMap;
        this.context = context;
    }


    protected abstract Marker buildMarker(M markerData);

    public void addView(M markerData) {
        this.markerData = markerData;
        marker = buildMarker(markerData);
        addMarkSuccess(markerData);

    }

    protected abstract void addMarkSuccess(M markerData);


    public void removeView() {
        if (null != marker) {
            marker.destroy();
            marker = null;

        }

        infoWindow = null;
        viewHolder = null;
        infoWindowListener = null;


    }


    /**
     * 初始化加载对话框
     */
    public void initMarkInfoWindowAdapter(T data, @LayoutRes final int layoutid, InfoWindowListener<T> listener) {

        this.infoData = data;
        infoWindowListener = listener;
        map.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });

        map.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @SuppressLint("InflateParams")
            @Override
            public View getInfoWindow(Marker marker) {

                if (infoWindow == null) {
                    infoWindow = LayoutInflater.from(getContext()).inflate(
                            layoutid, null);

                    viewHolder = new MapInfoWindowViewHolder(infoWindow);
                    reshInfoWindowData(getInfoData());

                }


                return infoWindow;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
        if (null != getMarker() && data.isShowWindow()) {
            getMarker().showInfoWindow();
        }
    }


    public interface InfoWindowListener<T> {
        void bindData(MapInfoWindowViewHolder viewHolder, T data);
    }

    public AMap getMap() {
        return map;
    }


    public Context getContext() {
        return context;
    }


    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Marker getMarker() {
        return marker;
    }


    public void reshInfoWindowData(T data) {
        this.infoData = data;
        if (null != viewHolder && null != infoWindowListener) {
            infoWindowListener.bindData(viewHolder, data);
        }
    }

    public View getInfoWindow() {
        return infoWindow;
    }

    public void setInfoWindow(View infoWindow) {
        this.infoWindow = infoWindow;
    }
}
