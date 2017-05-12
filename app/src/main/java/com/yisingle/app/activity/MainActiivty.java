package com.yisingle.app.activity;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.widget.FrameLayout;

import com.yisingle.app.R;
import com.yisingle.app.base.BaseActivity;
import com.yisingle.app.fragment.FastCarFragment;
import com.yisingle.app.fragment.SideDrawerFragment;
import com.yisingle.app.map.location.service.GuardService;
import com.yisingle.app.map.location.service.LocationService;


import butterknife.BindView;

/**
 * Created by jikun on 17/5/5.
 */

public class MainActiivty extends BaseActivity {


    @BindView(R.id.fl_menu)
    FrameLayout fl_menu;//侧滑栏
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {


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
        }

        startLocationService();

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
