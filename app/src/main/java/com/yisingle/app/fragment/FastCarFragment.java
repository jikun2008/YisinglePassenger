package com.yisingle.app.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import com.yisingle.app.base.BasePresenter;
import com.yisingle.app.data.FastCarTypeData;
import com.yisingle.app.dialog.LocationNameQueryDialogFragment;
import com.yisingle.app.event.LocationEvent;
import com.yisingle.app.map.MapRxManager;
import com.yisingle.app.map.help.AMapLocationHelper;
import com.yisingle.app.map.utils.CoordinateTransUtils;
import com.yisingle.app.map.utils.RegeocodeAddressInfoUtils;
import com.yisingle.app.map.view.CenterMapMarkerView;
import com.yisingle.app.map.view.LocationMapMarkerView;
import com.yisingle.baselibray.baseadapter.RecyclerAdapter;
import com.yisingle.baselibray.baseadapter.viewholder.RecyclerViewHolder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

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

    @BindView(R.id.ll_no_choose_des)
    LinearLayout ll_no_choose_des;


    @BindView(R.id.recyclerView_choose_car_type)
    RecyclerView recyclerView_choose_car_type;
    List<FastCarTypeData> fastCarTypeListData;
    RecyclerAdapter<FastCarTypeData> choose_car_type_adapter;


    @BindView(R.id.recyclerView_price)
    RecyclerView recyclerView_price;
    RecyclerAdapter<FastCarTypeData> price_adapter;

    private boolean isMapMove = false;


    private AMapLocationHelper aMapLocationHelper;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_fast_car;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        //initViews比initMapCreate先执行

        initChooseCarTypeRecyclerView();

        initPriceRecyclerView();


    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected boolean isregisterEventBus() {
        return true;
    }


    @OnClick(R.id.iv_location)
    public void loctionToMapView() {

        if (null != locationMapMarkerView && null != locationMapMarkerView.getLocMarker()) {
            LatLng latLng = locationMapMarkerView.getLocMarker().getPosition();


            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        }


    }


    @Override
    public void initMapLoad() {
        setMapUiSetting();
        locationMapMarkerView = new LocationMapMarkerView(getContext());
        centerMapMarkerView = new CenterMapMarkerView(getContext());
        centerMapMarkerView.addMarkViewToMap(getaMap());


        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {


            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (!isMapMove) {
                    tv_start_place.setText("正在获取上车点");

                    centerMapMarkerView.stopInfoWindowLoading();
                    centerMapMarkerView.hideInfoWindow();
                    centerMapMarkerView.stopFrameAnimation();
                }
                isMapMove = true;


            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                isMapMove = false;

                centerMapMarkerView.showInfoWindowLoading();
                centerMapMarkerView.startFrameAnimation(5);

                MapRxManager.getRegeocodeAddressObservable(getContext(), cameraPosition.target.latitude, cameraPosition.target.longitude)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<RegeocodeAddress>() {
                            @Override
                            public void onCompleted() {
                                centerMapMarkerView.stopInfoWindowLoading();
                                centerMapMarkerView.stopFrameAnimation();


                            }

                            @Override
                            public void onError(Throwable e) {
                                //Log.e("测试代码", "测试代码--onError=" + e.toString());
                                tv_start_place.setText("你从哪出发");
                                centerMapMarkerView.stopInfoWindowLoading();
                                centerMapMarkerView.stopFrameAnimation();

                            }

                            @Override
                            public void onNext(RegeocodeAddress regeocodeAddress) {
                                Log.e("测试代码", "测试代码\n" + RegeocodeAddressInfoUtils.getRegeocodeAddress(regeocodeAddress));
                                tv_start_place.setText(RegeocodeAddressInfoUtils.getSimpleSitename(regeocodeAddress));

                            }
                        });

            }
        });

        //这个是在获取地图amap的回调方法
        aMapLocationHelper = new AMapLocationHelper(getContext());
        aMapLocationHelper.startSingleLocate(new AMapLocationHelper.OnLocationGetListeneAdapter() {
            @Override
            public void onLocationGetSuccess(AMapLocation location) {


                if (locationMapMarkerView.isAddMarkViewToMap()) {
                    locationMapMarkerView.setMarkerViewPosition(location);
                } else {
                    locationMapMarkerView.addMarkerViewToMap(location, aMap);
                }

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
            if (null == locationMapMarkerView) return;
            if (locationMapMarkerView.isAddMarkViewToMap()) {
                locationMapMarkerView.setMarkerViewPosition(event.getMapLocation());
            } else {
                locationMapMarkerView.addMarkerViewToMap(event.getMapLocation(), aMap);
            }

        }


    }

    @OnClick({R.id.ll_end, R.id.ll_start})

    public void chooseWhere(View view) {
        LocationNameQueryDialogFragment fragment = new LocationNameQueryDialogFragment();
        switch (view.getId()) {

            case R.id.ll_start:
                fragment.show(getChildFragmentManager(), LocationNameQueryDialogFragment.class.getSimpleName());
                break;
            case R.id.ll_end:

                fragment.show(getChildFragmentManager(), LocationNameQueryDialogFragment.class.getSimpleName());
                break;
        }

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
        });


    }

    private void initPriceRecyclerView() {


        List<FastCarTypeData> dataList = new ArrayList<>();
        dataList.add(FastCarTypeData.createNormalTypeData(true, "普通"));
        dataList.add(FastCarTypeData.createExcellentTypeData(false, "优享型"));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        recyclerView_price.setLayoutManager(gridLayoutManager);

        price_adapter = new RecyclerAdapter<FastCarTypeData>(dataList, R.layout.adapter_fast_car_price) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, int position, FastCarTypeData item) {

            }
        };

        recyclerView_price.setAdapter(price_adapter);

        price_adapter.setOnItemClickListener((position, item) -> {
            Log.e("123", "123");

        });


    }

}
