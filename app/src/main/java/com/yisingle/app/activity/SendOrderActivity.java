package com.yisingle.app.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.yisingle.app.R;
import com.yisingle.app.base.BaseMapActivity;
import com.yisingle.app.base.BasePresenter;
import com.yisingle.app.data.DriverData;
import com.yisingle.app.data.MapPointData;
import com.yisingle.app.data.OrderData;
import com.yisingle.app.map.view.CarMapMarkerView;
import com.yisingle.app.map.view.LocationMapMarkerView;
import com.yisingle.app.map.view.LocationMapMarkerView.LocationMapMarkerData;
import com.yisingle.app.map.view.PointMapMarkerView;
import com.yisingle.app.map.view.PointMapMarkerView.PointMapMarkerData;
import com.yisingle.baselibray.baseadapter.RecyclerAdapter;
import com.yisingle.baselibray.baseadapter.viewholder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jikun on 17/6/15.
 */

public class SendOrderActivity extends BaseMapActivity {

    @BindView(R.id.textureMapView)
    TextureMapView textureMapView;

    @BindView(R.id.driver_recycleView)
    RecyclerView driver_recycleView;

    @BindView(R.id.ll_driver_have)
    LinearLayout ll_driver_have;

    @BindView(R.id.bt_cancle_order)
    Button bt_cancle_order;

    @BindView(R.id.tv_driver_name)
    TextView tv_driver_name;//司机姓名

    @BindView(R.id.tv_driver_car_number)
    TextView tv_driver_car_number;//车牌号

    @BindView(R.id.tv_driver_car_name)
    TextView tv_driver_car_name;//车型

    protected LocationMapMarkerView locationMapMarkerView;

    private PointMapMarkerView startPointMapMarkerView;

    private CarMapMarkerView carMapMarkerView;

    private OrderData sendOrderData;


    private MapPointData startMapPointData;
    private MapPointData endMapPointData;

    private RecyclerAdapter<String> driverAdapter;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_send_order;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {


        sendOrderData = getIntent().getParcelableExtra("SendOrderData");

        LatLng startLatLng = new LatLng(Double.parseDouble(sendOrderData.getStartLatitude()), Double.parseDouble(sendOrderData.getStartLongitude()));

        startMapPointData = MapPointData.createStartMapPointData(sendOrderData.getStartPlaceName(), startLatLng);

        LatLng endLatLnt = new LatLng(Double.parseDouble(sendOrderData.getEndLatitude()), Double.parseDouble(sendOrderData.getEndLongitude()));

        endMapPointData = MapPointData.createStartMapPointData(sendOrderData.getEndPlaceName(), endLatLnt);


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
        //initMapLoad在initViews之后执行
        setMapUiSetting();


        locationMapMarkerView = new LocationMapMarkerView(getaMap(), getApplicationContext());
        locationMapMarkerView.addView(LocationMapMarkerData.createData(false));
        startPointMapMarkerView = new PointMapMarkerView(getaMap(), getApplicationContext());

        carMapMarkerView = new CarMapMarkerView(getaMap(), getApplicationContext());


        startPointMapMarkerView.addView(PointMapMarkerData.createData(startMapPointData.getRes(), startMapPointData.getLatLng(), startMapPointData.getText(), 40));


        //////////


        //////////
        if (sendOrderData.getOrderState() == OrderData.State.WAIT_NEW || sendOrderData.getOrderState() == OrderData.State.WAIT_OLD) {
            setTitle("等待应答", true);
            showWaitView();
            startPointMapMarkerView.initMarkInfoWindowAdapter();

            startPointMapMarkerView.addCircleViewToMap(startMapPointData.getLatLng(), getaMap());


            startPointMapMarkerView.startCountTime();
            startPointMapMarkerView.moveToCamera();
        } else {
            startPointMapMarkerView.removeCircle();
            startPointMapMarkerView.stopCountTime();

            double latitude = Double.parseDouble(sendOrderData.getDriverEntity().getLatitude());
            double longitude = Double.parseDouble(sendOrderData.getDriverEntity().getLongitude());
            LatLng latLng = new LatLng(latitude, longitude);
            carMapMarkerView.addView(new CarMapMarkerView.CarMapMarkerData(latLng, R.mipmap.car));
            carMapMarkerView.initMarkInfoWindowAdapter();
            carMapMarkerView.moveToCamera();
            setTitle("等待接驾", true);
            showDriverView(sendOrderData);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != locationMapMarkerView) {
            locationMapMarkerView.removeView();
        }
        if (null != startPointMapMarkerView) {
            startPointMapMarkerView.removeView();
        }
        if (null != carMapMarkerView) {
            carMapMarkerView.removeView();
        }
    }

    @OnClick(R.id.bt_cancle_order)
    public void cancleOrder() {
        finish();
    }


    private void showDriverView(OrderData orderData) {

        if (null != orderData.getDriverEntity()) {

            DriverData driverData = orderData.getDriverEntity();
            tv_driver_name.setText(driverData.getDriverName());

            // tv_driver_car_number.setText(driverData.get);//车牌号

            //  tv_driver_car_name.setText(driverData.get);

        }


        ll_driver_have.setVisibility(View.VISIBLE);

        bt_cancle_order.setVisibility(View.GONE);


        List<String> data = new ArrayList<>();
        data.add("取消行程");
        data.add("行程分享");
        data.add("需要帮助");
        data.add("紧急求助");
        data.add("交通上报");
        driverAdapter = new RecyclerAdapter<String>(data, R.layout.adapter_driver_function) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, int position, String item) {
                holder.setText(R.id.tv_info, item);
            }
        };

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };


        driver_recycleView.setLayoutManager(gridLayoutManager);

        driver_recycleView.setAdapter(driverAdapter);
        driverAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object item) {
                startPointMapMarkerView.removeCircle();
                startPointMapMarkerView.stopCountTime();
            }
        });


    }

    private void showWaitView() {
        ll_driver_have.setVisibility(View.GONE);

        bt_cancle_order.setVisibility(View.VISIBLE);
    }
}
