package com.map.library.base;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.map.library.view.MapRouteView;
import com.yisingle.baselibray.base.BaseFragment;
import com.yisingle.baselibray.base.BasePresenter;

import java.lang.reflect.Field;

/**
 * Created by jikun on 17/5/10.
 */

public abstract class BaseMapFragment<T extends BasePresenter> extends BaseFragment<T> {
    private TextureMapView mapView;

    private MapRouteView mapRouteView;

    protected AMap aMap;


    protected abstract TextureMapView getTextureMapView();

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initMapView(savedInstanceState);

    }

    private void initMapView(Bundle savedInstanceState) {
        mapView = getTextureMapView();


        if (mapView != null) {
            aMap = mapView.getMap();
            aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                @Override
                public void onMapLoaded() {
                    initMapLoad();
                }
            });
            mapView.onCreate(savedInstanceState);
            mapRouteView = new MapRouteView(aMap, getContext());
        }
    }


    /**
     * 初始化地图一些initMapCreate 在onActivityCreated后调用
     * initViews比initMapCreate先执行
     */

    protected abstract void initMapLoad();


    /**
     * 方法必须重写
     */
    @Override

    public void onResume() {
        super.onResume();
        if (null != mapView) {
            mapView.onResume();
        }

    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        if (null != mapView) {
            mapView.onPause();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != mapView) {
            mapView.onSaveInstanceState(outState);
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        removeMapRouteView();
        if (null != mapView) {
            mapView.onDestroy();
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    protected void setMapUiSetting() {

        UiSettings uiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        uiSettings.setZoomControlsEnabled(false);//设置是否允许显示缩放按钮
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);//设置Logo在底部右下角
        //uiSettings.setLogoBottomMargin(DisplayUtil.dip2px(getContext(), 120));//设置Logo距离底部120dp;
        //禁止旋转手势
        uiSettings.setRotateGesturesEnabled(false);
        //禁止倾斜手势
        uiSettings.setTiltGesturesEnabled(false);

    }


    /**
     * 移除地图上的marker和路线
     */
    protected void removeMapRouteView() {
        if (null != mapRouteView) {
            mapRouteView.removeRoute();
        }
        if (null != getaMap()) {
            getaMap().clear();
        }

    }

    public MapRouteView getMapRouteView() {
        return mapRouteView;
    }

    public void setMapRouteView(MapRouteView mapRouteView) {
        this.mapRouteView = mapRouteView;
    }

    public AMap getaMap() {
        return aMap;
    }

    public void moveToCamera(LatLng center) {
        //设置缩放级别
        float zoom = 17;
        if (null != getaMap()) {
            //zoom - 缩放级别，[3-20]。
            getaMap().animateCamera(CameraUpdateFactory.newLatLngZoom(center, zoom));
        }

    }

    /**
     * 在导航的地图MapView上移动视角
     */
    public void moveToCamera(LatLng start, LatLng end) {

        moveToCamera(start, end, new Rect(0, 0, 0, 0));

    }

    /**
     * 在导航的地图MapView上移动视角
     */
    public void moveToCamera(LatLng start, LatLng end, Rect rect) {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();


        builder.include(start);
        builder.include(end);
        LatLngBounds latLngBounds = builder.build();
       // newLatLngBoundsRect(LatLngBounds latlngbounds, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom)
        //newLatLngBoundsRect(LatLngBounds latlngbounds,
        //int paddingLeft,设置经纬度范围和mapView左边缘的空隙。
        //int paddingRight,设置经纬度范围和mapView右边缘的空隙
        //int paddingTop,设置经纬度范围和mapView上边缘的空隙。
        //int paddingBottom)设置经纬度范围和mapView下边缘的空隙。
        if (null != getaMap()) {
            getaMap().animateCamera(CameraUpdateFactory.newLatLngBoundsRect(latLngBounds, rect.left,  rect.right,rect.top, rect.bottom));
        }


    }

}
