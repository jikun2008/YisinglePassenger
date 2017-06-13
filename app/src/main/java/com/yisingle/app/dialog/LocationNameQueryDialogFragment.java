package com.yisingle.app.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.amap.api.maps.model.LatLng;
import com.yisingle.app.R;
import com.yisingle.app.base.BaseDialogFragment;
import com.yisingle.app.base.BasePresenter;
import com.yisingle.app.fragment.CityChooseFragment;
import com.yisingle.app.fragment.HisDestinationFragment;
import com.yisingle.app.map.help.AMapLocationHelper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jikun on 17/5/23.
 */

public class LocationNameQueryDialogFragment extends BaseDialogFragment {

    @BindView(R.id.bt_choose_city)
    Button bt_choose_city;
    @BindView(R.id.et_city_name)
    EditText et_city_name;
    @BindView(R.id.et_destination_name)
    EditText et_destination_name;

    private int callBackcode = 0;


    private OnChoosePlaceListener<ChooseData> onChoosePlaceListener;

    private CityChooseFragment cityChooseFragment;

    private HisDestinationFragment hisDestinationFragment;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.dialog_fragment_location_name_query;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);//设置不挤压窗口

    }


    private void showCityEditText(boolean isshow) {
        bt_choose_city.setVisibility(isshow ? View.GONE : View.VISIBLE);
        et_city_name.setVisibility(isshow ? View.VISIBLE : View.GONE);
        if (isshow) {
            et_city_name.requestFocus();
        }
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        String city = AMapLocationHelper.getLastKnownLocation(getContext()).getCity();

        bt_choose_city.setText(city);

        showHisDestinationFragment();


        et_city_name.addTextChangedListener(mTextWatcher);

        et_destination_name.addTextChangedListener(mdesTextWatcher);


    }


    private void showCityChooseFragment() {
        String tag = CityChooseFragment.class.getSimpleName();
        FragmentManager fragmentManager = getChildFragmentManager();

        if (null == fragmentManager.findFragmentByTag(tag)) {
            cityChooseFragment = CityChooseFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, cityChooseFragment, tag)
                    .commitAllowingStateLoss();
            showCityEditText(true);
            hisDestinationFragment = null;


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
            hisDestinationFragment = HisDestinationFragment.newInstance(callBackcode);
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, hisDestinationFragment, tag)
                    .commitAllowingStateLoss();
            showCityEditText(false);
            hisDestinationFragment.setChoosePlaceListener((hisDestinationData, code) -> {

                onChoosePlaceListener.onchoose(new ChooseData(hisDestinationData.getLatLng(), hisDestinationData.getName()), code);
                dismissAllowingStateLoss();
            });
            cityChooseFragment = null;
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

    @OnClick(R.id.et_destination_name)
    public void choosHisDestinationFragment() {
        showHisDestinationFragment();
    }


    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (null != cityChooseFragment && null != s) {
                cityChooseFragment.reshDataByfilterStr(s.toString());
            }
        }
    };

    private TextWatcher mdesTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (null != hisDestinationFragment && null != s) {

                String city = bt_choose_city.getText().toString();
                hisDestinationFragment.reshdata(city, s.toString());

            }
        }
    };


    public OnChoosePlaceListener<ChooseData> getOnChoosePlaceListener() {
        return onChoosePlaceListener;
    }

    public void setOnChoosePlaceListener(OnChoosePlaceListener<ChooseData> onChoosePlaceListener) {
        this.onChoosePlaceListener = onChoosePlaceListener;
    }

    public interface OnChoosePlaceListener<T> {
        void onchoose(T t, int callBackcode);

    }


    public class ChooseData {
        private LatLng latLng;
        private String name;

        public ChooseData(LatLng latLng, String name) {
            this.latLng = latLng;
            this.name = name;
        }

        public LatLng getLatLng() {
            return latLng;
        }

        public void setLatLng(LatLng latLng) {
            this.latLng = latLng;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    public int show(FragmentTransaction transaction, String tag, int callBackcode) {
        this.callBackcode = callBackcode;
        return show(transaction, tag);
    }

    public void show(FragmentManager manager, String tag, int callBackcode) {
        this.callBackcode = callBackcode;
        show(manager, tag);
    }
}

