package com.yisingle.app.fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.orhanobut.logger.Logger;
import com.yisingle.app.R;
import com.yisingle.app.base.BaseMapFragment;
import com.yisingle.app.event.LocationEvent;
import com.yisingle.app.map.help.AMapLocationHelper;
import com.yisingle.app.map.utils.CoordinateTransUtils;
import com.yisingle.app.map.utils.RegeocodeAddressInfoUtils;
import com.yisingle.app.rx.MapRxUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by jikun on 17/5/10.
 */


public class FastCarFragment extends BaseMapFragment {
    @BindView(R.id.textureMapView)
    TextureMapView textureMapView;
    @BindView(R.id.tv_start_place)
    TextView tv_start_place;
    @BindView(R.id.tv_destination_place)
    TextView tv_destination_place;

    private boolean isMapMove = false;


    private AMapLocationHelper aMapLocationHelper;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_fast_car;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        //initViews比initMapCreate先执行


    }

    @Override
    protected boolean isregisterEventBus() {
        return true;
    }


    @OnClick(R.id.iv_location)
    public void loctionToMapView() {

        if (null != locationMarkerHelper && null != locationMarkerHelper.getLocMarker()) {
            LatLng latLng = locationMarkerHelper.getLocMarker().getPosition();
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        }


    }


    @Override
    public void initMapLoad() {
        setMapUiSetting();
        initCenterMarkerHelper();
        initLocationMarkerHelper();


        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {


            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (!isMapMove) {
                    tv_start_place.setText("正在获取上车点");
                    centerMarkerHelper.stopFrameAnimation();
                    centerMarkerHelper.hideInfoWindow();
                }
                isMapMove = true;


            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                isMapMove = false;

                centerMarkerHelper.startFrameAnimation(5);

                MapRxUtils.getRegeocodeAddressObservable(getContext(), cameraPosition.target.latitude, cameraPosition.target.longitude)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<RegeocodeAddress>() {
                            @Override
                            public void onCompleted() {
//

                                centerMarkerHelper.stopFrameAnimation();

                            }

                            @Override
                            public void onError(Throwable e) {
//                                Log.e("测试代码", "测试代码--onError=" + e.toString());
                                tv_start_place.setText("你从哪出发");
                                centerMarkerHelper.stopFrameAnimation();

                            }

                            @Override
                            public void onNext(RegeocodeAddress regeocodeAddress) {
                                Log.e("测试代码", "测试代码\n" + RegeocodeAddressInfoUtils.getRegeocodeAddress(regeocodeAddress));
                                tv_start_place.setText(RegeocodeAddressInfoUtils.getSimpleSitename(regeocodeAddress));
                                centerMarkerHelper.showInfoWindowCompleteData(regeocodeAddress.getFormatAddress());
                            }
                        });

            }
        });

        //这个是在获取地图amap的回调方法
        aMapLocationHelper = new AMapLocationHelper(getContext());
        aMapLocationHelper.startSingleLocate(new AMapLocationHelper.OnLocationGetListeneAdapter() {
            @Override
            public void onLocationGetSuccess(AMapLocation location) {


                locationMarkerHelper.setLocationMarkerPosition(location, aMap);
                LatLng latLng = CoordinateTransUtils.changToLatLng(location);
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));


            }

        });


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
        textureMapView = null;
        aMapLocationHelper.destroyLocation();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationEventAccept(LocationEvent event) {
        Logger.e("onLocationEventAccept()");
        if (event.getCode() == LocationEvent.Code.SUCCESS) {
            locationMarkerHelper.setLocationMarkerPosition(event.getMapLocation(), aMap);
        }
    }


}
