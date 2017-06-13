package com.yisingle.app.mvp.presenter;

import android.content.Context;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.yisingle.app.base.BasePresenter;
import com.yisingle.app.map.MapRxManager;
import com.yisingle.app.map.utils.RegeocodeAddressInfoUtils;
import com.yisingle.app.mvp.IFastCar;
import com.yisingle.app.rx.ApiSubscriber;

import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by jikun on 17/6/13.
 */

public class FastCarPresenter extends BasePresenter<IFastCar.FastCarView> implements IFastCar.FastCarPresenter {
    public FastCarPresenter(IFastCar.FastCarView view) {
        super(view);
    }

    @Override
    public void getRegeocodeAddress(Context context, LatLng latLng) {
        MapRxManager.getRegeocodeAddressObservable(context, latLng.latitude, latLng.longitude)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiSubscriber<RegeocodeAddress>(mView, true, true) {
                    @Override
                    public void onNext(RegeocodeAddress regeocodeAddress) {
                        String address = RegeocodeAddressInfoUtils.getSimpleSitename(regeocodeAddress);

                        mView.onAddressSuccess(regeocodeAddress, address,latLng);
                    }
                });
    }

}

