package com.yisingle.app.activity;

import android.os.Bundle;

import com.amap.api.maps.TextureMapView;
import com.yisingle.app.R;
import com.yisingle.app.base.BaseMapActivity;
import com.yisingle.app.base.BasePresenter;
import com.yisingle.app.data.MapPointData;
import com.yisingle.app.map.view.LocationMapMarkerView;
import com.yisingle.app.map.view.PointMapMarkerView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jikun on 17/6/15.
 */

public class SendOrderActivity extends BaseMapActivity {

    @BindView(R.id.textureMapView)
    TextureMapView textureMapView;

    protected LocationMapMarkerView locationMapMarkerView;

    private PointMapMarkerView startPointMapMarkerView;

    private MapPointData startMapPointData;

    private MapPointData endMapPointData;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_send_order;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setTitle("等待应答", true);

        startMapPointData = getIntent().getParcelableExtra("start");
        endMapPointData = getIntent().getParcelableExtra("end");

    }

    @Override
    protected boolean isregisterEventBus() {
        return false;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected TextureMapView getTextureMapView() {
        return textureMapView;
    }

    @Override
    protected void initMapLoad() {
        setMapUiSetting();
        locationMapMarkerView = new LocationMapMarkerView(getApplicationContext());
        locationMapMarkerView.addMarkViewToMap(getaMap(), false);
        startPointMapMarkerView = new PointMapMarkerView(getApplicationContext(), 0);


        startPointMapMarkerView.addMarkViewToMap(startMapPointData.getLatLng(), startMapPointData.getRes(), getaMap(), false);
        startPointMapMarkerView.addCircleViewToMap(startMapPointData.getLatLng(), getaMap());
        startPointMapMarkerView.moveToCamera(aMap, startMapPointData.getLatLng());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != locationMapMarkerView) {
            locationMapMarkerView.removeMarkerViewFromMap();
        }
        if (null != startPointMapMarkerView) {
            startPointMapMarkerView.removeMarkerViewFromMap();
        }
    }

    @OnClick(R.id.bt_cancle_order)
    public void test() {
        // locationMapMarkerView.addMarkViewToMap(getaMap(), true);
        //startPointMapMarkerView.addMarkViewToMap(startMapPointData.getLatLng(), startMapPointData.getRes(), getaMap(), false);
    }
}
