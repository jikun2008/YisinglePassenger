package com.yisingle.app.activity;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yisingle.app.R;
import com.yisingle.app.base.BasePassengerActivity;
import com.yisingle.app.base.Constant;
import com.yisingle.app.data.MainTabData;
import com.yisingle.app.data.UserData;
import com.yisingle.app.event.DoLoginEvent;
import com.yisingle.app.event.UserDataEvent;
import com.yisingle.app.fragment.FastCarFragment;
import com.yisingle.app.fragment.LoginRegisterDialogFragment;
import com.yisingle.app.fragment.SideDrawerFragment;
import com.yisingle.app.mvp.IRegister;
import com.yisingle.app.mvp.presenter.RegisterPresenterImpl;
import com.yisingle.app.service.GuardService;
import com.yisingle.app.service.LocationService;

import com.yisingle.baselibray.baseadapter.RecyclerAdapter;
import com.yisingle.baselibray.baseadapter.viewholder.RecyclerViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jikun on 17/5/5.
 */


public class MainActiivty extends BasePassengerActivity<RegisterPresenterImpl> implements IRegister.RegisterView {


    @BindView(R.id.fl_menu)
    FrameLayout fl_menu;//侧滑栏
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;
    @BindView(R.id.tab_recyclerView)
    RecyclerView tab_recyclerView;

    private SideDrawerFragment sideDrawerFragment;

    List<MainTabData> dataList = new ArrayList<>();


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }


    @Override
    protected void initViews(Bundle savedInstanceState) {
        initTabRecyclerView();
        SPUtils.getInstance().put(Constant.IS_LOGIN_SUCCESS, false);
        setTitle("主界面", v -> {
            boolean isLoginSuccess = SPUtils.getInstance().getBoolean(Constant.IS_LOGIN_SUCCESS, false);
            if (isLoginSuccess) {
                sideDrawerFragment.loadView();
                if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                    drawer_layout.closeDrawer(GravityCompat.START);
                } else {
                    drawer_layout.openDrawer(GravityCompat.START);
                }
            } else {
                new LoginRegisterDialogFragment().show(getSupportFragmentManager(), LoginRegisterDialogFragment.class.getSimpleName());
            }


        });


        if (savedInstanceState == null) {
            sideDrawerFragment = SideDrawerFragment.newInstance();
            loadRootFragment(R.id.fl_menu, sideDrawerFragment);
            loadRootFragment(R.id.fl_container, FastCarFragment.newInstance());
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
            Log.e("dothing", "dothing");
        }

        startLocationService();
        autoLogin();

    }

    @Override
    protected boolean isregisterEventBus() {
        return true;
    }

    @Override
    protected RegisterPresenterImpl createPresenter() {
        return new RegisterPresenterImpl(this);
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
        SPUtils.getInstance().put(Constant.IS_LOGIN_SUCCESS, false);
    }

    @Override
    public void onBackPressedSupport() {

        finish();

    }

    @Override
    public void registerSuccess(UserData data) {

    }


    private void autoLogin() {

        String phone = SPUtils.getInstance().getString(Constant.PHONE_NUM, "");
        String password = SPUtils.getInstance().getString(Constant.PASS_WORD, "");
        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {
            mPresenter.login(phone, password, RegisterPresenterImpl.TYPE.Login);

        }

    }

    @Override
    public void loginSuccess(UserData data) {
        SPUtils.getInstance().put(Constant.LOGIN_PASSENGER_ID, data.getId());
        SPUtils.getInstance().put(Constant.PHONE_NUM, data.getPhonenum());
        SPUtils.getInstance().put(Constant.PASS_WORD, data.getPassword());
        SPUtils.getInstance().put(Constant.IS_LOGIN_SUCCESS, true);
        EventBus.getDefault().post(new UserDataEvent(data));
        ToastUtils.showShort("自动登陆成功");
    }

    /**
     * 请登录
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void doToLogin(DoLoginEvent event) {
        findViewById(R.id.ib_left).performClick();
    }


    /**
     * @param title    标题
     * @param backFlag 是否显示返回按钮
     */
    @SuppressWarnings("unused")
    protected void setTitle(String title, boolean backFlag) {
        if (findViewById(R.id.titleBar) == null) return;
        ((TextView) findViewById(R.id.tv_title)).setText(title);
        if (backFlag) {
            findViewById(R.id.ib_left).setOnClickListener(view -> finish());
        } else {
            findViewById(R.id.ib_right).setVisibility(View.GONE);
        }
    }


    /**
     * @param title               标题
     * @param leftOnclickListener 点击监听器
     */
    protected void setTitle(String title, View.OnClickListener leftOnclickListener) {
        if (findViewById(R.id.titleBar) == null) return;
        ((TextView) findViewById(R.id.tv_title)).setText(title);

        findViewById(R.id.ib_left).setVisibility(View.VISIBLE);
        findViewById(R.id.ib_left).setOnClickListener(v -> {
            if (leftOnclickListener != null) {
                leftOnclickListener.onClick(v);
            }
        });

    }
}
