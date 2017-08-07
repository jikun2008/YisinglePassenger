package com.yisingle.app.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.yisingle.app.R;
import com.yisingle.app.base.Constant;
import com.yisingle.app.data.SideData;
import com.yisingle.app.utils.BitMapUtils;

import com.yisingle.baselibray.base.BaseFragment;
import com.yisingle.baselibray.base.BasePresenter;
import com.yisingle.baselibray.baseadapter.RecyclerAdapter;
import com.yisingle.baselibray.baseadapter.viewholder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class SideDrawerFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recycleView;


    @BindView(R.id.iv_header)
    ImageView iv_header;

    @BindView(R.id.tv_passenger_name)
    TextView tv_passenger_name;

    RecyclerAdapter<SideData> adapter;

    private List<SideData> stringList;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_side_drawer;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {


    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected boolean isregisterEventBus() {
        return false;
    }


    public static SideDrawerFragment newInstance() {

        Bundle args = new Bundle();

        SideDrawerFragment fragment = new SideDrawerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initRecycleView() {
        stringList = new ArrayList<>();
        stringList.add(new SideData("行程", R.mipmap.side_trip_icon));
        stringList.add(new SideData("钱包", R.mipmap.side_wallet_icon));
        stringList.add(new SideData("客服", R.mipmap.side_service_icon));
        stringList.add(new SideData("设置", R.mipmap.side_setting_icon));
        stringList.add(new SideData("注销", R.mipmap.side_setting_icon));

        recycleView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        adapter = new RecyclerAdapter<SideData>(stringList, R.layout.adapter_side_drawer) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, int position, SideData item) {
                holder.setText(R.id.tv_show_info, item.getText());
                holder.setImageResource(R.id.iv_header, item.getDrawableId());

            }
        };
        recycleView.setAdapter(adapter);
        adapter.setOnItemClickListener((position, item) -> {

            if (position == 4) {
                SPUtils.getInstance().put(Constant.PHONE_NUM, "");
                SPUtils.getInstance().put(Constant.PASS_WORD, "");
                SPUtils.getInstance().put(Constant.IS_LOGIN_SUCCESS, false);
                getActivity().finish();
            }
        });

    }

    public void loadView() {
        String passengerName =   SPUtils.getInstance().getString(Constant.PHONE_NUM, "");
        tv_passenger_name.setText(passengerName);

        Bitmap rouudBitmap = BitMapUtils.getRoundBitMap(getResources(), R.mipmap.touxiang);
        iv_header.setImageBitmap(rouudBitmap);
        initRecycleView();
    }


}
