package com.yisingle.app.mvp;


import android.content.Context;

import com.amap.api.maps.model.LatLng;
import com.yisingle.app.data.ChoosePointData;
import com.yisingle.app.data.MapPointData;
import com.yisingle.app.data.OrderData;
import com.yisingle.baselibray.base.BaseView;

/**
 * 登录页接口
 * Created by yu on 2016/11/2.
 */
public interface IFastCar {
    interface FastCarView extends BaseView {

       void onAddressSuccess(ChoosePointData choosePointData);

        void onSendOrderSuccess(OrderData sendOrderData);

    }

    interface FastCarPresenter {
        void getRegeocodeAddress(Context context,  LatLng latLng,int type);

        void sendOrder(String phoneNum, MapPointData startMapPointData, MapPointData endMapPointData,int type);

    }

}