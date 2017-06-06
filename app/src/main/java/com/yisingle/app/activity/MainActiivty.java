package com.yisingle.app.activity;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.FrameLayout;

import com.yisingle.app.R;
import com.yisingle.app.base.BaseActivity;
import com.yisingle.app.base.BasePresenter;
import com.yisingle.app.data.MainTabData;
import com.yisingle.app.dialog.LocationNameQueryDialogFragment;
import com.yisingle.app.fragment.FastCarFragment;
import com.yisingle.app.fragment.SideDrawerFragment;
import com.yisingle.app.service.GuardService;
import com.yisingle.app.service.LocationService;
import com.yisingle.baselibray.baseadapter.RecyclerAdapter;
import com.yisingle.baselibray.baseadapter.viewholder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jikun on 17/5/5.
 */


public class MainActiivty extends BaseActivity {


    @BindView(R.id.fl_menu)
    FrameLayout fl_menu;//侧滑栏
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;
    @BindView(R.id.tab_recyclerView)
    RecyclerView tab_recyclerView;

    List<MainTabData> dataList = new ArrayList<>();


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }


    @Override
    protected void initViews(Bundle savedInstanceState) {
        initTabRecyclerView();

        setTitle("主界面", v -> {

            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START);
            } else {
                drawer_layout.openDrawer(GravityCompat.START);
            }
        });


        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_menu, SideDrawerFragment.newInstance());
            loadRootFragment(R.id.fl_container, FastCarFragment.newInstance());
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
            Log.e("dothing", "dothing");
        }

        startLocationService();


    }

    @Override
    protected boolean isregisterEventBus() {
        return false;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    private void initTabRecyclerView() {

        dataList.add(new MainTabData("快车", true));
        dataList.add(new MainTabData("专车"));
        dataList.add(new MainTabData("公交"));
        dataList.add(new MainTabData("代驾"));
        dataList.add(new MainTabData("顺风车"));
        dataList.add(new MainTabData("出租车"));
        dataList.add(new MainTabData("试驾"));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        tab_recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerAdapter<MainTabData> adapter = new RecyclerAdapter<MainTabData>(dataList, R.layout.adapter_tab_main) {
            @Override
            protected void onBindData(RecyclerViewHolder holder, int position, MainTabData item) {

                holder.setText(R.id.tv_tab_name, item.getTitle());
                holder.setSelected(R.id.tv_tab_name, item.isChoose());


            }
        };

        tab_recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((position, item) -> {


            for (MainTabData mainTabData : dataList) {
                mainTabData.setChoose(false);

            }
            dataList.get(position).setChoose(true);

            adapter.notifyDataSetChanged();
        });

    }

    private void startLocationService() {
        GuardService.startService(getApplicationContext());
        LocationService.startService(getApplicationContext());
    }

    private void stopLocationService() {
        GuardService.stopService(getApplicationContext());
        LocationService.stopService(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationService();
    }
}
