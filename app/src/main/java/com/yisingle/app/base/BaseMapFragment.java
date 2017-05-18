package com.yisingle.app.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.yisingle.app.map.help.CenterMarkerHelper;
import com.yisingle.app.map.help.LocationMarkerHelper;
import com.yisingle.app.utils.DisplayUtil;

import java.lang.reflect.Field;

/**
 * Created by jikun on 17/5/10.
 */

public abstract class BaseMapFragment extends BaseFrament {
    private TextureMapView mapView;
    protected AMap aMap;

    protected LocationMarkerHelper locationMarkerHelper;
    protected CenterMarkerHelper centerMarkerHelper;


    protected abstract TextureMapView getTextureMapView();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mapView = getTextureMapView();


        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
            aMap = mapView.getMap();
            aMap.setOnMapLoadedListener(this::initMapLoad);
        }


    }


    protected void initCenterMarkerHelper() {
        centerMarkerHelper = new CenterMarkerHelper(getContext());
        centerMarkerHelper.initMarkInfoWindowAdapter(aMap);
        centerMarkerHelper.addCenterMarkToMap(aMap);
    }


    private void destroyCenterMarkerHelper() {
        if (null != centerMarkerHelper) {
            centerMarkerHelper.destroy();
        }

    }

    protected void initLocationMarkerHelper() {
        locationMarkerHelper = new LocationMarkerHelper(getContext());

    }


    private void destroyLocationMarkerHelper() {
        if (null != locationMarkerHelper) {
            locationMarkerHelper.destroy();
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
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        destroyCenterMarkerHelper();
        destroyLocationMarkerHelper();
        mapView.onDestroy();

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
        uiSettings.setLogoBottomMargin(DisplayUtil.dip2px(getContext(), 120));//设置Logo距离底部120dp;

    }

    @SuppressWarnings("unused")
    public AMap getaMap() {
        return aMap;
    }

    @SuppressWarnings("unused")
    public void setaMap(AMap aMap) {
        this.aMap = aMap;
    }
}
