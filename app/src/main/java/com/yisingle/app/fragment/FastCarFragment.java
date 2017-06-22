package com.yisingle.app.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.yisingle.app.R;
import com.yisingle.app.activity.SendOrderActivity;
import com.yisingle.app.base.BaseMapFragment;
import com.yisingle.app.data.ChoosePointData;
import com.yisingle.app.data.FastCarPriceData;
import com.yisingle.app.data.FastCarTypeData;
import com.yisingle.app.data.MapPointData;
import com.yisingle.app.dialog.LocationNameQueryDialogFragment;
import com.yisingle.app.map.view.BaseMarkerData;
import com.yisingle.app.map.view.CenterMapMarkerView;
import com.yisingle.app.map.view.CenterMapMarkerView.CenterWindowData;
import com.yisingle.app.map.view.LocationMapMarkerView;
import com.yisingle.app.map.view.NearbyCarMapListMarkerView;
import com.yisingle.app.map.view.PointMapMarkerView.PointMapMarkerData;
import com.yisingle.app.map.view.StartAndEndPointListMarkerView;
import com.yisingle.app.mvp.IFastCar;
import com.yisingle.app.mvp.presenter.FastCarPresenter;
import com.yisingle.app.utils.SpannableStringUtils;
import com.yisingle.baselibray.baseadapter.RecyclerAdapter;
import com.yisingle.baselibray.baseadapter.viewholder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jikun on 17/5/10.
 */


public class FastCarFragment extends BaseMapFragment<FastCarPresenter> implements IFastCar.FastCarView, AMap.OnCameraChangeListener {
    @BindView(R.id.textureMapView)
    TextureMapView textureMapView;
    @BindView(R.id.tv_start_place)
    TextView tv_start_place;
    @BindView(R.id.tv_destination_place)
    TextView tv_destination_place;

    @BindView(R.id.ll_no_choose_des)
    LinearLayout ll_no_choose_des;

    @BindView(R.id.ll_have_choose_des)
    LinearLayout ll_have_choose_des;


    @BindView(R.id.recyclerView_choose_car_type)
    RecyclerView recyclerView_choose_car_type;
    List<FastCarTypeData> fastCarTypeListData;
    RecyclerAdapter<FastCarTypeData> choose_car_type_adapter;


    @BindView(R.id.recyclerView_price)
    RecyclerView recyclerView_price;
    List<FastCarPriceData> fastCarPriceDataList;
    RecyclerAdapter<FastCarPriceData> price_adapter;

    LocationNameQueryDialogFragment locationNameQueryDialogFragment;


    private boolean isMapMove = false;

    private boolean isNoCamraChange = false;


    private StartAndEndPointListMarkerView startAndEndPointMarkerView;

    protected LocationMapMarkerView locationMapMarkerView;
    protected CenterMapMarkerView centerMapMarkerView;

    private NearbyCarMapListMarkerView nearbyCarMapMarkerView;

    private MapPointData startMapPointData;

    private MapPointData endMapPointData;


    @Override
    public void onDestroy() {
        super.onDestroy();
        textureMapView = null;
        removeMarkerViewFromMap();

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_fast_car;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        //initViews比initMapCreate先执行


        initChooseCarTypeRecyclerView();

        initPriceRecyclerView();

        reshPriceData(false);

        showNoHaveDes();


    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (isNoCamraChange) {
            return;
        }
        if (!isMapMove) {
            tv_start_place.setText("正在获取上车点");
            centerMapMarkerView.hideInfoWindow();
        }
        isMapMove = true;
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        isMapMove = false;
        if (isNoCamraChange) {
            return;
        }

        mPresenter.getRegeocodeAddress(getContext(), cameraPosition.target);
    }

    @Override
    public void showLoading() {
        Log.e("测试代码", "测试代码FastCarFragment+showLoading");
        centerMapMarkerView.showLoading(5);
    }

    @Override
    public void dismissLoading() {
        Log.e("测试代码", "测试代码FastCarFragment+dismissLoading");
        centerMapMarkerView.stopLoading();
    }

    @Override
    public void onError() {
        Log.e("测试代码", "测试代码FastCarFragment+onError");
        tv_start_place.setText("你从哪出发");
        centerMapMarkerView.reshInfoWindowData(CenterWindowData.createError());
    }

    @Override
    public void onAddressSuccess(ChoosePointData data) {
        Log.e("测试代码", "测试代码FastCarFragment+onAddressSuccess");
        centerMapMarkerView.reshInfoWindowData(CenterWindowData.createSuccess());
        tv_start_place.setText(data.getSimpleAddress());
        startMapPointData = MapPointData.createStartMapPointData(data.getSimpleAddress(), data.getLatLng());


        nearbyCarMapMarkerView.addView(NearbyCarMapListMarkerView.changeList(data.getCarPositionDatas()));
    }


    @Override
    protected boolean isregisterEventBus() {
        return false;
    }


    private void showHaveDes() {
        ll_have_choose_des.setVisibility(View.VISIBLE);
        ll_no_choose_des.setVisibility(View.GONE);

    }

    private void showNoHaveDes() {
        ll_have_choose_des.setVisibility(View.GONE);
        ll_no_choose_des.setVisibility(View.VISIBLE);

    }


    @OnClick(R.id.iv_location)
    public void loctionToMapView() {
        if (startMapPointData != null && endMapPointData != null) {
            startAndEndPointMarkerView.moveToCamera();
        } else {
            if (null != locationMapMarkerView && null != locationMapMarkerView.getMarker()) {
                locationMapMarkerView.startLocationToView();
            }
        }

    }

    private void loctionToMapViewByPosition(LatLng latLng) {

        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));//zoom - 缩放级别，[3-20]。

    }


    @Override
    public void initMapLoad() {
        setMapUiSetting();
        startAndEndPointMarkerView = new StartAndEndPointListMarkerView(getaMap(), getContext());
        locationMapMarkerView = new LocationMapMarkerView(getaMap(), getContext());
        centerMapMarkerView = new CenterMapMarkerView(getaMap(), getContext());

        nearbyCarMapMarkerView = new NearbyCarMapListMarkerView(getaMap(), getContext());
        addLoctionCenterMarkerViewToMap();
        aMap.setOnCameraChangeListener(this);


    }


    public static FastCarFragment newInstance() {
        Bundle args = new Bundle();
        FastCarFragment fragment = new FastCarFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected FastCarPresenter createPresenter() {
        return new FastCarPresenter(this);
    }

    @Override
    protected TextureMapView getTextureMapView() {
        return textureMapView;
    }


    @OnClick({R.id.ll_end, R.id.ll_start})
    public void chooseWhere(View view) {
        locationNameQueryDialogFragment = new LocationNameQueryDialogFragment();
        locationNameQueryDialogFragment.setOnChoosePlaceListener((chooseData, callBackcode) -> {
            switch (callBackcode) {
                case 1:
                    loctionToMapViewByPosition(chooseData.getLatLng());
                    break;
                case 2:

                    if (startMapPointData != null) {


                        endMapPointData = MapPointData.createEndMapPointData(chooseData.getName(), chooseData.getLatLng());
                        removeMarkerViewFromMap();

                        addLoctionStartAndEndMarkerViewToMap(startMapPointData, endMapPointData, aMap);

                        tv_destination_place.setText(chooseData.getName());
                        showHaveDes();
                    }
                    break;
            }
        });

        switch (view.getId()) {

            case R.id.ll_start:
                locationNameQueryDialogFragment.show(getChildFragmentManager(), LocationNameQueryDialogFragment.class.getSimpleName(), 1);
                break;
            case R.id.ll_end:
                locationNameQueryDialogFragment.show(getChildFragmentManager(), LocationNameQueryDialogFragment.class.getSimpleName(), 2);
                break;
        }


    }


    public void addLoctionCenterMarkerViewToMap() {
        isNoCamraChange = false;
        centerMapMarkerView.addView(new BaseMarkerData());
        centerMapMarkerView.initMarkInfoWindowAdapter();
        locationMapMarkerView.addView(LocationMapMarkerView.LocationMapMarkerData.createData(true));

    }


    public void addLoctionStartAndEndMarkerViewToMap(MapPointData startData, MapPointData endData, AMap aMap) {
        isNoCamraChange = true;
        locationMapMarkerView.addView(LocationMapMarkerView.LocationMapMarkerData.createData(false));

        List<PointMapMarkerData> list = new ArrayList<>();
        PointMapMarkerData data = PointMapMarkerData.createData(startData.getRes(), startData.getLatLng(), startData.getText(), 40);
        PointMapMarkerData data1 = PointMapMarkerData.createData(endData.getRes(), endData.getLatLng(), endData.getText(), 40);
        list.add(data);
        list.add(data1);
        startAndEndPointMarkerView.addView(list);
        startAndEndPointMarkerView.moveToCamera();


    }

    public void removeMarkerViewFromMap() {
        if (null != locationMapMarkerView) {
            locationMapMarkerView.removeView();
        }
        if (null != centerMapMarkerView) {
            centerMapMarkerView.removeView();
        }
        if (null != startAndEndPointMarkerView) {
            startAndEndPointMarkerView.removeView();
        }
        if (null != nearbyCarMapMarkerView) {
            nearbyCarMapMarkerView.removeView();
        }
        getaMap().clear();
    }


    private void initChooseCarTypeRecyclerView() {
        fastCarTypeListData = new ArrayList<>();
        fastCarTypeListData.add(FastCarTypeData.createNormalTypeData(true, "普通"));
        fastCarTypeListData.add(FastCarTypeData.createExcellentTypeData(false, "优享型"));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView_choose_car_type.addItemDecoration(new DividerItemDecoration(getContext(), GridLayoutManager.VERTICAL));

        recyclerView_choose_car_type.setLayoutManager(gridLayoutManager);

        choose_car_type_adapter = new RecyclerAdapter<FastCarTypeData>(fastCarTypeListData, R.layout.adapter_fast_car_choose_type) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, int position, FastCarTypeData item) {

                Drawable drawable = getResources().getDrawable(item.getTypeIcon());
                holder.setDrawableLeft(R.id.tv_fast_car_average, drawable);
                holder.setText(R.id.tv_fast_car_average, item.getTypeName());
                holder.setSelected(R.id.tv_fast_car_average, item.isselector());
                holder.setVisibility(R.id.view_line, item.isselector() ? View.VISIBLE : View.GONE);
                holder.setSelected(R.id.view_line, item.isselector());

            }
        };

        recyclerView_choose_car_type.setAdapter(choose_car_type_adapter);
        choose_car_type_adapter.setOnItemClickListener((position, item) -> {
            for (int i = 0; i < fastCarTypeListData.size(); i++) {
                if (i == position) {
                    fastCarTypeListData.get(i).setIsselector(true);
                } else {
                    fastCarTypeListData.get(i).setIsselector(false);
                }
            }

            choose_car_type_adapter.notifyDataSetChanged();

            if (position == 0) {
                reshPriceData(false);
            } else {
                reshPriceData(true);
            }

        });


    }


    private void initPriceRecyclerView() {


        price_adapter = new RecyclerAdapter<FastCarPriceData>(null, R.layout.adapter_fast_car_price) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, int position, FastCarPriceData item) {

                if (item.getType() == FastCarPriceData.PriceType.NOCarpooling || item.getType() == FastCarPriceData.PriceType.Carpooling) {

                    ColorStateList colorStateList = getResources().getColorStateList(R.color.text_main_selector);
                    holder.setTextColor(R.id.tv_normal_car_price, colorStateList);
                    holder.setTextColor(R.id.tv_type, colorStateList);
                } else {


                    holder.setTextColor(R.id.tv_normal_car_price, getResources().getColor(R.color.black_text));
                    holder.setTextColor(R.id.tv_type, getResources().getColor(R.color.gray_text));
                }

                holder.setText(R.id.tv_type, item.getTypeName());
                holder.setSelected(R.id.tv_type, item.isselector());

                SpannableStringBuilder spannableStringBuilder = new SpannableStringUtils.Builder()
                        .append("约")
                        .append(item.getPrice()).setBold().setFontProportion(2)
                        .append("元").create();
                holder.setText(R.id.tv_normal_car_price, spannableStringBuilder);

                holder.setSelected(R.id.tv_normal_car_price, item.isselector());

                SpannableStringBuilder discont = new SpannableStringUtils.Builder()
                        .append("优惠卷已抵扣")
                        .append(item.getDiscountPrice()).setForegroundColor(getResources().getColor(R.color.orange_text))
                        .append("元").create();

                holder.setText(R.id.tv_discount, discont);


            }
        };

        recyclerView_price.setAdapter(price_adapter);

        price_adapter.setOnItemClickListener((position, item) -> {
            for (int i = 0; i < fastCarPriceDataList.size(); i++) {
                if (i == position) {
                    fastCarPriceDataList.get(i).setIsselector(true);
                } else {
                    fastCarPriceDataList.get(i).setIsselector(false);
                }
            }

            price_adapter.notifyDataSetChanged();

        });


    }

    private void reshPriceData(boolean isExcellentCar) {

        GridLayoutManager gridLayoutManager = null;
        if (isExcellentCar) {
            gridLayoutManager = new GridLayoutManager(getContext(), 1) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            fastCarPriceDataList = new ArrayList<>();
            fastCarPriceDataList.add(FastCarPriceData.createExcellentPriceData(false, "精选好司机，车大品质优", "9.99", "5.46"));

        } else {
            gridLayoutManager = new GridLayoutManager(getContext(), 2) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            fastCarPriceDataList = new ArrayList<>();
            fastCarPriceDataList.add(FastCarPriceData.createCarpoolingPriceData(true, "拼车", "6.66", "5.00"));
            fastCarPriceDataList.add(FastCarPriceData.createNOCarpoolingPriceData(false, "不拼车", "8.88", "5.32"));
        }


        recyclerView_price.setLayoutManager(gridLayoutManager);
        price_adapter.refreshWithNewData(fastCarPriceDataList);

    }

    @Override
    public boolean onBackPressedSupport() {
        if (null != endMapPointData) {
            endMapPointData = null;
            removeMarkerViewFromMap();
            addLoctionCenterMarkerViewToMap();
            showNoHaveDes();
            return true;
        } else {

            return false;
        }


    }

    @OnClick(R.id.bt_start_user_car)
    public void toSendOrderActivity() {
        Intent intent = new Intent();
        intent.putExtra("start", startMapPointData);
        intent.putExtra("end", endMapPointData);
        intent.setClass(getActivity(), SendOrderActivity.class);
        getActivity().startActivity(intent);
    }

}
