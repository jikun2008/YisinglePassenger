package com.yisingle.app.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yisingle.app.R;
import com.yisingle.app.base.BaseFrament;
import com.yisingle.app.base.BasePresenter;
import com.yisingle.app.data.CityModel;
import com.yisingle.app.data.HisDestinationData;
import com.yisingle.app.decoration.HisDestinationItemDecoration;
import com.yisingle.baselibray.baseadapter.RecyclerAdapter;
import com.yisingle.baselibray.baseadapter.viewholder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jikun on 17/6/1.
 */

public class HisDestinationFragment extends BaseFrament {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    private RecyclerAdapter<HisDestinationData> adapter;

    private List<HisDestinationData> dataList = new ArrayList<>();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_hisdestination;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        testData();
        initRecyclerView();
    }

    public static HisDestinationFragment newInstance() {

        Bundle args = new Bundle();

        HisDestinationFragment fragment = new HisDestinationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void testData() {

        dataList.add(HisDestinationData.createHomeHisDestinationData("家", "家里"));
        dataList.add(HisDestinationData.createCompanyHisDestinationData("公司", "东方希望天祥国际广场"));
        dataList.add(HisDestinationData.createNormalHisDestinationData("天府广场", "天府广场春熙路"));
        dataList.add(HisDestinationData.createNormalHisDestinationData("双流机场", "双流机场618"));
        dataList.add(HisDestinationData.createNormalHisDestinationData("时间飞逝", "是否"));
        dataList.add(HisDestinationData.createNormalHisDestinationData("是否", "爽肤水"));
        dataList.add(HisDestinationData.createNormalHisDestinationData("是方式", "发是"));
        dataList.add(HisDestinationData.createNormalHisDestinationData("爽肤水", "爽肤水"));
        dataList.add(HisDestinationData.createNormalHisDestinationData("是否是", "方式"));
        dataList.add(HisDestinationData.createNormalHisDestinationData("爽肤水", "方式方法"));
    }


    private void initRecyclerView() {
        adapter = new RecyclerAdapter<HisDestinationData>(dataList, R.layout.adapter_history_destination) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, int position, HisDestinationData item) {
                holder.setText(R.id.tv_destination_name, item.getName());
                holder.setText(R.id.tv_destination_allname, item.getAllName());
                holder.setImageResource(R.id.iv_icon_head, item.getIcon());

            }
        };
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int size;
                if (position == 0 || position == 1) {
                    size = 1;
                } else {
                    size = 2;
                }
                return size;
            }
        };
        gridLayoutManager.setSpanSizeLookup(spanSizeLookup);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new HisDestinationItemDecoration(getContext(), 1));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected boolean isregisterEventBus() {
        return false;
    }
}
