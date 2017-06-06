package com.yisingle.app.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.yisingle.app.R;
import com.yisingle.app.base.BaseFrament;
import com.yisingle.app.base.BasePresenter;
import com.yisingle.app.data.CityModel;
import com.yisingle.app.decoration.SuspensionDecoration;
import com.yisingle.app.map.help.AMapLocationHelper;
import com.yisingle.app.map.utils.CityUtil;
import com.yisingle.app.widget.IndexBar.IndexBar;
import com.yisingle.app.widget.IndexBar.helper.GaoDeCityIIndexBarDataHelper;
import com.yisingle.baselibray.baseadapter.RecyclerAdapter;
import com.yisingle.baselibray.baseadapter.viewholder.RecyclerViewHolder;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by jikun on 17/5/31.
 */

public class CityChooseFragment extends BaseFrament {


    @BindView(R.id.tv_choose_city)
    TextView tv_choose_city;

    @BindView(R.id.tvSideBarHint)
    TextView tvSideBarHint;

    @BindView(R.id.indexBar)
    IndexBar indexBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    ArrayList<CityModel> groupedCityList;

    RecyclerAdapter<CityModel> adapter;

    SuspensionDecoration suspensionDecoration;

    LinearLayoutManager linearLayoutManager;

    private OnChooseCityListener onChooseCityListener;

    private CityModel currentLoctionCityModel;


    public OnChooseCityListener getOnChooseCityListener() {
        return onChooseCityListener;
    }

    public void setOnChooseCityListener(OnChooseCityListener onChooseCityListener) {
        this.onChooseCityListener = onChooseCityListener;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_city_choose;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        initRecyclerView();
        initIndexBar(linearLayoutManager);

        initdata();

        initLocationCityData();
    }

    public static CityChooseFragment newInstance() {

        Bundle args = new Bundle();

        CityChooseFragment fragment = new CityChooseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initIndexBar(LinearLayoutManager linearLayoutManager) {
        indexBar.setmPressedShowTextView(tvSideBarHint)//设置HintTextView
                .setDataHelper(new GaoDeCityIIndexBarDataHelper())
                .setNeedRealIndex(true)//设置需要真实的索引
                .setSourceDatasAlreadySorted(true)
                .setmLayoutManager(linearLayoutManager);//设置RecyclerView的LayoutManager

    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        adapter = new RecyclerAdapter<CityModel>(groupedCityList, R.layout.adapter_city_choose) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, int position, CityModel item) {
                holder.setText(R.id.tv_cityname_name, item.getCity());

            }
        };

        suspensionDecoration = new SuspensionDecoration(getContext(), groupedCityList);

        recyclerView.addItemDecoration(suspensionDecoration);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = getResources().getDrawable(R.drawable.divider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener((position, item) -> {
            if (null != onChooseCityListener) {
                onChooseCityListener.onChooseCity(this, groupedCityList.get(position));

            }

        });


    }

    private void initdata() {
        CityUtil.setHotCities(null);
        groupedCityList = CityUtil.getGroupCityList(getContext());
        indexBar.setmSourceDatas(groupedCityList)//设置数据
                .invalidate();

        adapter.refreshWithNewData(groupedCityList);
        suspensionDecoration.setmDatas(groupedCityList);


    }

    private void initLocationCityData() {
        AMapLocation location = AMapLocationHelper.getLastKnownLocation(getContext());
        if (null != location && null != location.getCityCode()) {
            currentLoctionCityModel = CityUtil.getLocationCityModel(getContext(), location.getCityCode());
            if (null != currentLoctionCityModel) {
                tv_choose_city.setText("当前城市:" + currentLoctionCityModel.getCity());
            }
        }

        tv_choose_city.setOnClickListener(v -> {
            if (null != currentLoctionCityModel) {
                onChooseCityListener.onChooseCity(CityChooseFragment.this, currentLoctionCityModel);
            }

        });

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected boolean isregisterEventBus() {
        return false;
    }

    public interface OnChooseCityListener {

        void onChooseCity(CityChooseFragment fragment, CityModel cityModel);


    }

    public void reshDataByfilterStr(String filterStr) {
        groupedCityList = CityUtil.getGroupCityList(getContext(), filterStr);
        adapter.refreshWithNewData(groupedCityList);
    }
}
