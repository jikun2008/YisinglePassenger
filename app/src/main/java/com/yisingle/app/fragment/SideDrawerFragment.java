package com.yisingle.app.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yisingle.app.R;
import com.yisingle.app.base.BaseFrament;
import com.yisingle.baselibray.baseadapter.RecyclerAdapter;
import com.yisingle.baselibray.baseadapter.viewholder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class SideDrawerFragment extends BaseFrament {
    @BindView(R.id.recyclerView)
    RecyclerView recycleView;
    RecyclerAdapter<String> adapter;

    private List<String> stringList;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_side_drawer;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {


        initRecycleView();

    }


    public static SideDrawerFragment newInstance() {

        Bundle args = new Bundle();

        SideDrawerFragment fragment = new SideDrawerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initRecycleView() {
        stringList = new ArrayList<>();
        stringList.add("行程");
        stringList.add("钱包");
        stringList.add("客服");
        stringList.add("设置");

        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerAdapter<String>(stringList, R.layout.adapter_side_drawer) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, int position, String item) {
                holder.setText(R.id.tv_show_info, item);

            }
        };
        recycleView.setAdapter(adapter);
        adapter.setOnItemClickListener((position, item) -> {


        });

    }
}
