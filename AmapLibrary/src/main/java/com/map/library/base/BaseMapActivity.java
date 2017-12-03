package com.map.library.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.yisingle.baselibray.base.BaseActivity;
import com.yisingle.baselibray.base.BasePresenter;
import com.yisingle.baselibray.utils.DisplayUtil;

/**
 * Created by jikun on 17/5/10.
 */

public abstract class BaseMapActivity<T extends BasePresenter> extends BaseActivity<T> {
    private TextureMapView mapView;
    protected AMap aMap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView = getTextureMapView();


        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
            aMap = mapView.getMap();
            aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                @Override
                public void onMapLoaded() {
                    initMapLoad();
                }
            });
        }
    }

    protected abstract TextureMapView getTextureMapView();


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
        mapView.onDestroy();

    }


    protected void setMapUiSetting() {

        UiSettings uiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        uiSettings.setZoomControlsEnabled(false);//设置是否允许显示缩放按钮
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);//设置Logo在底部右下角
        uiSettings.setLogoBottomMargin(DisplayUtil.dip2px(getApplicationContext(), 120));//设置Logo距离底部120dp;
        //旋转手势
        uiSettings.setRotateGesturesEnabled(false);
        //倾斜手势
        uiSettings.setTiltGesturesEnabled(false);

    }


    public AMap getaMap() {
        return aMap;
    }

}