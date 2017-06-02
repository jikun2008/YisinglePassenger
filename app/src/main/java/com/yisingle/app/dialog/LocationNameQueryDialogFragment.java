package com.yisingle.app.dialog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yisingle.app.R;
import com.yisingle.app.base.BaseDialogFragment;
import com.yisingle.app.base.BasePresenter;
import com.yisingle.app.data.CityModel;
import com.yisingle.app.data.HisDestinationData;
import com.yisingle.app.decoration.HisDestinationItemDecoration;
import com.yisingle.app.fragment.CityChooseFragment;
import com.yisingle.app.fragment.HisDestinationFragment;
import com.yisingle.app.map.help.AMapLocationHelper;
import com.yisingle.baselibray.baseadapter.RecyclerAdapter;
import com.yisingle.baselibray.baseadapter.viewholder.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jikun on 17/5/23.
 */

public class LocationNameQueryDialogFragment extends BaseDialogFragment {

    @BindView(R.id.bt_choose_city)
    Button bt_choose_city;

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

        String city = AMapLocationHelper.getLastKnownLocation(getContext()).getCity();

        bt_choose_city.setText(city);

        showHisDestinationFragment();


    }


    private void showCityChooseFragment() {
        String tag = CityChooseFragment.class.getSimpleName();
        FragmentManager fragmentManager = getChildFragmentManager();

        if (null == fragmentManager.findFragmentByTag(tag)) {
            CityChooseFragment cityChooseFragment = CityChooseFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, cityChooseFragment, tag)
                    .commitAllowingStateLoss();

            cityChooseFragment.setOnChooseCityListener((fragment, cityModel) -> {
                Log.e("测试代码", "测试代码cityModel=" + cityModel.toString());
                showHisDestinationFragment();
                bt_choose_city.setText(cityModel.getCity());
            });
        }

    }

    private void showHisDestinationFragment() {
        String tag = HisDestinationFragment.class.getSimpleName();
        FragmentManager fragmentManager = getChildFragmentManager();
        if (null == fragmentManager.findFragmentByTag(tag)) {
            HisDestinationFragment hisDestinationFragment = HisDestinationFragment.newInstance();
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, hisDestinationFragment, tag)
                    .commitAllowingStateLoss();
        }

    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected boolean isregisterEventBus() {
        return false;
    }


    @OnClick(R.id.bt_choose_city)
    public void chooseCityFragment() {
        showCityChooseFragment();
    }

    @OnClick(R.id.bt_cancle)
    public void closeDialog() {
        dismissAllowingStateLoss();
    }


}
