package com.yisingle.app.fragment;

import android.os.Bundle;

import com.yisingle.app.R;
import com.yisingle.app.base.BaseFrament;
import com.yisingle.app.base.BasePresenter;

/**
 * Created by jikun on 17/5/31.
 */

public class CityChooseFragment extends BaseFrament {
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_city_choose;
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
}
