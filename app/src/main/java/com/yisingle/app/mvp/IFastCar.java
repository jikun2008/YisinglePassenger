package com.yisingle.app.mvp;


import android.content.Context;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.yisingle.app.base.BaseView;

/**
 * 登录页接口
 * Created by yu on 2016/11/2.
 */
public interface IFastCar {
    interface FastCarView extends BaseView {

       void onAddressSuccess(RegeocodeAddress regeocodeAddress,String simpleAddress, LatLng latLng);

    }

    interface FastCarPresenter {
        void getRegeocodeAddress(Context context,  LatLng latLng);
    }

}