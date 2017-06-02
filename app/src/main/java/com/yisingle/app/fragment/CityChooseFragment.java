package com.yisingle.app.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.yisingle.app.R;
import com.yisingle.app.base.BaseFrament;
import com.yisingle.app.base.BasePresenter;
import com.yisingle.app.data.CityModel;
import com.yisingle.app.decoration.SuspensionDecoration;
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
    protected void initViews(Bundle savedInstanceState) {

        initRecyclerView();
        initIndexBar(linearLayoutManager);

        initdata();
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
}
