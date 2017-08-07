package com.yisingle.app.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.yisingle.app.R;

import com.yisingle.baselibray.base.BaseDialogFragment;
import com.yisingle.baselibray.base.BasePresenter;
import com.yisingle.baselibray.baseadapter.SectionSimpleRecyclerAdapter;
import com.yisingle.baselibray.baseadapter.SectionSimpleRecyclerAdapter.GroupData;
import com.yisingle.baselibray.baseadapter.viewholder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jikun on 17/5/23.
 */

public class LocationNameQueryDialogFragmentTest extends BaseDialogFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private SectionSimpleRecyclerAdapter<String, String, String> adapter;

    private List<GroupData<String, String, String>> groupDataList = new ArrayList<>();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.dialog_fragment_location_name_query;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen);

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initRecyclerView();
        testData();

        adapter.refreshWithNewData(groupDataList);

    }

    private void testData() {
        List<String> data = new ArrayList<>();
        data.add("城市1");
        data.add("城市2");
        data.add("城市3");

        groupDataList.add(new GroupData<>("1", data, "1"));
        groupDataList.add(new GroupData<>("2", data, "1"));
        groupDataList.add(new GroupData<>("3", data, "1"));
        groupDataList.add(new GroupData<>("4", data, "1"));
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected boolean isregisterEventBus() {
        return false;
    }


    private void initRecyclerView() {


        adapter = new SectionSimpleRecyclerAdapter<String, String, String>(null, R.layout.adapter_header_city_pinying, R.layout.adapter_item_city_all_name) {
            @Override
            protected void onBindHeaderData(RecyclerViewHolder holder, int section, String item) {

                holder.setText(R.id.testINfo, item);

            }

            @Override
            protected void onBindItemData(RecyclerViewHolder holder, int section, int position, String item) {
                holder.setText(R.id.bt_test, item);
            }

            @Override
            protected void onBindFooterData(RecyclerViewHolder holder, int section, String item) {

            }
        };

        recyclerView.setLayoutManager(adapter.getGridLayoutManager(getContext(), 1));

        recyclerView.setAdapter(adapter);

    }
}
