package com.map.library.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.map.library.view.MapRouteView;
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

}
