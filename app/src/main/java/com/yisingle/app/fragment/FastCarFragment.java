package com.yisingle.app.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.orhanobut.logger.Logger;
import com.yisingle.app.R;
import com.yisingle.app.base.BaseMapFragment;
import com.yisingle.app.event.LocationEvent;
import com.yisingle.app.map.help.AMapLocationHelper;
import com.yisingle.app.map.utils.CoordinateTransUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jikun on 17/5/10.
 */

public class FastCarFragment extends BaseMapFragment {
    @BindView(R.id.textureMapView)
    TextureMapView textureMapView;

    @BindView(R.id.test)
    Button test;

    private AMapLocationHelper aMapLocationHelper;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_fast_car;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        //initViews比initMapCreate先执行


        EventBus.getDefault().register(this);


    }

    @Override
    public void initMapLoad() {
        //这个是在获取地图amap的回调方法
        aMapLocationHelper = new AMapLocationHelper(getContext());
        aMapLocationHelper.startSingleLocate();
        aMapLocationHelper.setOnLocationGetListener(new AMapLocationHelper.OnLocationGetListener() {
            @Override
            public void onLocationGetSuccess(AMapLocation location) {

                addCurrentMarkToMap(location);
                LatLng latLng = CoordinateTransUtils.changToLatLng(location);
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));

            }

            @Override
            public void onLocationGetFail(AMapLocation loc) {

            }
        });

        addCenterMarkToMap();


    }

    @Override
    public boolean isOpenSensorEventHelper() {
        //开启角度监测
        return true;
    }

    @Override
    public void onRotationChange(float angle) {
        if (null != locMarker) {
            locMarker.setRotateAngle(angle);
        }

    }

    public static FastCarFragment newInstance() {

        Bundle args = new Bundle();

        FastCarFragment fragment = new FastCarFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected TextureMapView getTextureMapView() {
        return textureMapView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        textureMapView = null;


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationEventAccept(LocationEvent event) {
        Logger.e("onLocationEventAccept()");
        if (event.getCode() == LocationEvent.Code.SUCCESS) {
            addCurrentMarkToMap(event.getMapLocation());
        }
    }


    @OnClick(R.id.test)
    public void test() {
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                R.mipmap.ic_launcher);

        locMarker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));

        EventBus.getDefault().post(new LocationEvent(LocationEvent.Code.SUCCESS, null));

    }

}
