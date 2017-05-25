package com.yisingle.app.activity;

import android.os.Bundle;

import com.yisingle.app.R;
import com.yisingle.app.base.BaseActivity;
import com.yisingle.app.base.BasePresenter;

public class LoginActivity extends BaseActivity {


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected boolean isregisterEventBus() {
        return false;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


}
