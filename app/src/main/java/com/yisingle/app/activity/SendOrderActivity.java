package com.yisingle.app.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.map.library.DistanceUtils;
import com.map.library.data.RouteData;
import com.map.library.view.polyline.MapPolylineView;
import com.yisingle.app.R;
import com.yisingle.app.base.BasePassengerMapActivity;
import com.yisingle.app.data.DriverData;
import com.yisingle.app.data.MapPointData;
import com.yisingle.app.data.OrderData;
import com.yisingle.app.event.OrderEvent;
import com.yisingle.app.event.PriceOrderEvent;
import com.yisingle.app.map.view.CarMapMarkerView;
import com.yisingle.app.map.view.CarMapMarkerView.CarMapWindowData;
import com.yisingle.app.map.view.LocationMapMarkerView;
import com.yisingle.app.map.view.LocationMapMarkerView.LocationMapMarkerData;
import com.yisingle.app.map.view.PointCircleMapMarkerView;
import com.yisingle.app.map.view.PointCircleMapMarkerView.PointMapMarkerData;
import com.yisingle.app.mvp.ISendOrder;
import com.yisingle.app.mvp.presenter.SendOrderPresenterImpl;
import com.yisingle.baselibray.baseadapter.RecyclerAdapter;
import com.yisingle.baselibray.baseadapter.viewholder.RecyclerViewHolder;
import com.yisingle.baselibray.utils.TimeDisUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jikun on 17/6/15.
 */

public class SendOrderActivity extends BasePassengerMapActivity<SendOrderPresenterImpl> implements ISendOrder.SendOrderView {

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

    private PointCircleMapMarkerView startPointMapMarkerView;

    private PointCircleMapMarkerView endPointMapMarkerView;

    private CarMapMarkerView carMapMarkerView;//车辆View

    private MapPolylineView mapPolylineView;//路线View

    private OrderData sendOrderData;


    private RecyclerAdapter<String> driverAdapter;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_send_order;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {


        initRecycleView();


    }

    @Override
    protected boolean isregisterEventBus() {
        return true;
    }

    @Override
    protected SendOrderPresenterImpl createPresenter() {
        return new SendOrderPresenterImpl(this);
    }

    @Override
    protected TextureMapView getTextureMapView() {
        return textureMapView;
    }

    @Override
    protected void initMapLoad() {
        //initMapLoad在initViews之后执行
        setMapUiSetting();


        sendOrderData = getIntent().getParcelableExtra("SendOrderData");


        createViewOnMap();
        showViewOnMap(sendOrderData, true);


    }


    private void showViewOnMap(OrderData orderData, boolean isshowLoadView) {


        removeViewOnMap();

        sendOrderData = orderData;
        LatLng startLatLng = new LatLng(Double.parseDouble(orderData.getStartLatitude()), Double.parseDouble(orderData.getStartLongitude()));

        MapPointData startMapPointData = MapPointData.createStartMapPointData(orderData.getStartPlaceName(), startLatLng);

        LatLng endLatLnt = new LatLng(Double.parseDouble(orderData.getEndLatitude()), Double.parseDouble(orderData.getEndLongitude()));

        MapPointData endMapPointData = MapPointData.createEndMapPointData(orderData.getEndPlaceName(), endLatLnt);

        LatLng driverlatLng = null;
        if (orderData.getDriver() != null) {
            double latitude = Double.parseDouble(orderData.getDriver().getLatitude());
            double longitude = Double.parseDouble(orderData.getDriver().getLongitude());
            driverlatLng = new LatLng(latitude, longitude);
        }


        switch (orderData.getOrderState()) {
            case OrderData.State.WAIT_NEW:
            case OrderData.State.WAIT_OLD:
                setTitle("等待应答", true);
                showDriverView(false, orderData);
                locationMapMarkerView.addView(LocationMapMarkerData.createData(false));
                startPointMapMarkerView.addView(PointMapMarkerData.createData(startMapPointData.getRes(), startMapPointData.getLatLng(), startMapPointData.getText(), 40));
                startPointMapMarkerView.initMarkInfoWindowAdapter();
                startPointMapMarkerView.addCircleViewToMap(startMapPointData.getLatLng(), getaMap());
                startPointMapMarkerView.startCountTime();
                startPointMapMarkerView.moveToCamera();
                break;

            case OrderData.State.HAVE_TAKE:
                setTitle("等待接驾", true);
                showDriverView(true, orderData);
                if (null != driverlatLng) {
                    //mPresenter.drawSingleRoute(getApplicationContext(), driverlatLng, startMapPointData.getLatLng());//使用
                    startPointMapMarkerView.addView(PointMapMarkerData.createData(startMapPointData.getRes(), startMapPointData.getLatLng(), startMapPointData.getText(), 40));
                    carMapMarkerView.addView(new CarMapMarkerView.CarMapMarkerData(driverlatLng, R.mipmap.car));
                    carMapMarkerView.moveToCamera();
                    mPresenter.drawSingleRoute(getApplicationContext(), driverlatLng, startLatLng, isshowLoadView);
                }
                locationMapMarkerView.addView(LocationMapMarkerData.createData(false));
                break;

            case OrderData.State.DRIVER_ARRIVE:
                setTitle("司机已到达", true);
                showDriverView(true, orderData);
                if (null != driverlatLng) {
                    startPointMapMarkerView.addView(PointMapMarkerData.createData(startMapPointData.getRes(), startMapPointData.getLatLng(), startMapPointData.getText(), 40));
                    carMapMarkerView.addView(new CarMapMarkerView.CarMapMarkerData(driverlatLng, R.mipmap.car));
                    carMapMarkerView.initMarkInfoWindowAdapter(CarMapWindowData.createSimpleData(true, CarMapWindowData.Type.DrivedAtSart));
                    carMapMarkerView.moveToCamera();
                    mPresenter.drawSingleRoute(getApplicationContext(), driverlatLng, startLatLng, isshowLoadView);
                }
                locationMapMarkerView.addView(LocationMapMarkerData.createData(false));
                break;

            case OrderData.State.PASSENGER_IN_CAR:
            case OrderData.State.PASSENGER_OUT_CAR:
            case OrderData.State.HAVE_COMPLETE:
                setTitle("行程中", true);
                showDriverView(true, orderData);
                if (null != driverlatLng) {
                    startPointMapMarkerView.addView(PointMapMarkerData.createData(startMapPointData.getRes(), startMapPointData.getLatLng(), startMapPointData.getText(), 40));
                    endPointMapMarkerView.addView(PointMapMarkerData.createData(endMapPointData.getRes(), endMapPointData.getLatLng(), endMapPointData.getText(), 40));
                    carMapMarkerView.addView(new CarMapMarkerView.CarMapMarkerData(driverlatLng, R.mipmap.car));
                    carMapMarkerView.moveToCamera();
                    mPresenter.drawSingleRoute(getApplicationContext(), driverlatLng, endLatLnt, isshowLoadView);
                }
                reshAdapterData();
                break;

        }
    }


    private void createViewOnMap() {

        if (null == locationMapMarkerView) {
            locationMapMarkerView = new LocationMapMarkerView(getaMap(), getApplicationContext());
        }

        if (null == startPointMapMarkerView) {
            startPointMapMarkerView = new PointCircleMapMarkerView(getaMap(), getApplicationContext());
        }

        if (null == endPointMapMarkerView) {
            endPointMapMarkerView = new PointCircleMapMarkerView(getaMap(), getApplicationContext());
        }

        if (null == carMapMarkerView) {
            carMapMarkerView = new CarMapMarkerView(getaMap(), getApplicationContext());

        }
        if (null == mapPolylineView) {
            mapPolylineView = new MapPolylineView(getApplicationContext(), getaMap());

        }

    }

    /**
     * 移除地图上的View除了定位的Marker
     */
    private void removeViewOnMap() {
        if (null != locationMapMarkerView) {
            locationMapMarkerView.removeView();

        }

        if (null != startPointMapMarkerView) {
            startPointMapMarkerView.removeView();

        }
        if (null != carMapMarkerView) {
            carMapMarkerView.removeView();

        }
        if (null != mapPolylineView) {
            mapPolylineView.removeLine();

        }

        if (null != endPointMapMarkerView) {
            endPointMapMarkerView.removeView();
        }
    }


    private void setViewOnMapToNull() {
        locationMapMarkerView = null;
        startPointMapMarkerView = null;
        carMapMarkerView = null;
        mapPolylineView = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeViewOnMap();
        setViewOnMapToNull();
    }

    @OnClick(R.id.bt_cancle_order)
    public void cancleOrder() {
        finish();
    }


    private void showDriverView(boolean isshow, OrderData orderData) {

        if (null != orderData.getDriver()) {

            DriverData driverData = orderData.getDriver();
            tv_driver_name.setText(driverData.getDriverName());

            // tv_driver_car_number.setText(driverData.get);//车牌号

            //  tv_driver_car_name.setText(driverData.get);

        }
        ll_driver_have.setVisibility(isshow ? View.VISIBLE : View.GONE);

        bt_cancle_order.setVisibility(isshow ? View.GONE : View.VISIBLE);
    }


    private void initRecycleView() {
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

            }
        });
    }

    private void reshAdapterData() {
        List<String> data = new ArrayList<>();
        data.add("取消行程");
        data.add("行程分享");
        data.add("需要帮助");
        data.add("紧急求助");
        driverAdapter.refreshWithNewData(data);
    }

    /**
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrderEvent(OrderEvent event) {
        sendOrderData = event.getOrderData();
        showViewOnMap(event.getOrderData(), true);
    }


    /**
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPriceOrderEvent(PriceOrderEvent event) {
        sendOrderData = event.getOrderData();


        double latitude = Double.parseDouble(sendOrderData.getDriver().getLatitude());
        double longitude = Double.parseDouble(sendOrderData.getDriver().getLongitude());
        LatLng driverlatLng = new LatLng(latitude, longitude);
        LatLng startLatLng = new LatLng(Double.parseDouble(sendOrderData.getStartLatitude()), Double.parseDouble(sendOrderData.getStartLongitude()));


        LatLng endLatLnt = new LatLng(Double.parseDouble(sendOrderData.getEndLatitude()), Double.parseDouble(sendOrderData.getEndLongitude()));

        switch (sendOrderData.getOrderState()) {

            case OrderData.State.HAVE_TAKE:
                mPresenter.drawSingleRoute(getApplicationContext(), driverlatLng, startLatLng, false);

                break;
            case OrderData.State.PASSENGER_IN_CAR:
            case OrderData.State.PASSENGER_OUT_CAR:
            case OrderData.State.HAVE_COMPLETE:
                mPresenter.drawSingleRoute(getApplicationContext(), driverlatLng, endLatLnt, false);

                break;
        }

    }


    @Override
    public void ondrawRouteSuccess(List<RouteData> routeResultList) {
        Log.e("测试代码", "测试代码ondrawRouteSuccess");
        if (null != routeResultList && routeResultList.size() > 0) {
            RouteData routeData = routeResultList.get(0);
            DriveRouteResult result = routeData.getDriveRouteResult();
            if (null != result && null != result.getPaths() && result.getPaths().size() > 0) {
                DrivePath drivePath = result.getPaths().get(0);
                mapPolylineView.removeLine();
                mapPolylineView.addLine(drivePath);

                String showtime = TimeDisUtils.changeSectoMin((int) drivePath.getDuration()) + "分钟";    //将预估的时间转换为显示的时间

                float distance = drivePath.getDistance();
                if (distance < 500) {
                    distance = 500;
                }
                String distanceInfo = DistanceUtils.getDistance((int) distance);

                CarMapWindowData.TripData tripData = new CarMapWindowData.TripData(showtime, distanceInfo, sendOrderData.getOrderPrice().doubleValue() + "");
                switch (sendOrderData.getOrderState()) {
                    case OrderData.State.HAVE_TAKE:
                        CarMapWindowData carMapWindowData = CarMapWindowData.createTripata(true, CarMapWindowData.Type.DrivingToStart, tripData);

                        if (null != carMapMarkerView.getInfoWindow()) {
                            DriverData driverData = sendOrderData.getDriver();
                            if (null != driverData.getLatitude() && null != driverData.getLongitude()) {
                                LatLng latLng = new LatLng(Double.parseDouble(driverData.getLatitude()), Double.parseDouble(driverData.getLongitude()));
                                carMapMarkerView.getMarker().setPosition(latLng);

                                carMapMarkerView.reshInfoWindowData(carMapWindowData);
                            }
                        } else {
                            carMapMarkerView.initMarkInfoWindowAdapter(carMapWindowData);
                        }


                        break;
                    case OrderData.State.PASSENGER_IN_CAR:
                    case OrderData.State.PASSENGER_OUT_CAR:
                    case OrderData.State.HAVE_COMPLETE:
                        CarMapWindowData tripWindowData = CarMapWindowData.createTripata(true, CarMapWindowData.Type.DriveAtTrip, tripData);
                        if (null != carMapMarkerView.getInfoWindow()) {
                            DriverData driverData = sendOrderData.getDriver();
                            if (null != driverData.getLatitude() && null != driverData.getLongitude()) {
                                LatLng latLng = new LatLng(Double.parseDouble(driverData.getLatitude()), Double.parseDouble(driverData.getLongitude()));
                                carMapMarkerView.getMarker().setPosition(latLng);

                                carMapMarkerView.reshInfoWindowData(tripWindowData);
                            }
                        } else {
                            carMapMarkerView.initMarkInfoWindowAdapter(tripWindowData);
                        }
                        break;
                }


            }
        }
    }

    @Override
    public void ondrawRouteFailed(Throwable e) {
        Log.e("测试代码", "测试代码ondrawRouteFailed  Throwable=" + e.toString());
    }
}
