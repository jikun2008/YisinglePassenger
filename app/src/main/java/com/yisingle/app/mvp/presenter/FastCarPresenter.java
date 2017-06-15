package com.yisingle.app.mvp.presenter;

import android.content.Context;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.yisingle.app.base.BasePresenter;
import com.yisingle.app.data.CarPositionData;
import com.yisingle.app.data.ChoosePointData;
import com.yisingle.app.http.ApiService;
import com.yisingle.app.http.RetrofitManager;
import com.yisingle.app.map.MapRxManager;
import com.yisingle.app.mvp.IFastCar;
import com.yisingle.app.rx.ApiSubscriber;
import com.yisingle.app.rx.RxUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by jikun on 17/6/13.
 */

public class FastCarPresenter extends BasePresenter<IFastCar.FastCarView> implements IFastCar.FastCarPresenter {
    public FastCarPresenter(IFastCar.FastCarView view) {
        super(view);
    }

    @Override
    public void getRegeocodeAddress(Context context, LatLng latLng) {

        Map<String, String> params = new HashMap<>();
        params.put("latitude", String.valueOf(latLng.latitude));
        params.put("longitude", String.valueOf(latLng.longitude));
        params.put("deviceId", "123");
        params.put("phonenum", "123");

        Observable<RegeocodeAddress> observable1 = MapRxManager.
                getRegeocodeAddressObservable(context, latLng.latitude, latLng.longitude)
                .subscribeOn(Schedulers.computation());

        Observable<List<CarPositionData>> observable2 = RetrofitManager.getInstance().createService(ApiService.class)
                .getNearByCarPosition(params)
                .filter(data -> mView != null)
                .subscribeOn(Schedulers.computation())
                .compose(RxUtils.parseResult());


        Observable.zip(observable1, observable2, (regeocodeAddress, carPositionDatas) -> {
            ChoosePointData data = ChoosePointData.createChoosePointData(latLng, regeocodeAddress, carPositionDatas);
            return data;
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ChoosePointData>() {
            @Override
            public void onStart() {
                super.onStart();
                mView.showLoading();
            }

            @Override
            public void onCompleted() {

                mView.dismissLoading();
            }

            @Override
            public void onError(Throwable e) {

                mView.dismissLoading();
                mView.onError();
            }

            @Override
            public void onNext(ChoosePointData choosePointData) {

                mView.onAddressSuccess(choosePointData);

            }
        });
//        MapRxManager.getRegeocodeAddressObservable(context, latLng.latitude, latLng.longitude)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new ApiSubscriber<RegeocodeAddress>(mView, true, true) {
//                    @Override
//                    public void onNext(RegeocodeAddress regeocodeAddress) {
//                        String address = RegeocodeAddressInfoUtils.getSimpleSitename(regeocodeAddress);
//
//                        mView.onAddressSuccess(regeocodeAddress, address, latLng);
//                    }
//                });
    }

    @Override
    public void getNearByCar() {
//        Map<String, String> params = new HashMap<>();
//        params.put("latitude", "123");
//        params.put("longitude", "123");
//        params.put("deviceId", "123");
//        params.put("phonenum", "123");
//        RetrofitManager.getInstance().createService(ApiService.class)
//                .getNearByCarPosition(params)
//                .filter(data -> mView != null)
//                .compose(RxUtils.apiChildTransformer())
//                .subscribe(new ApiSubscriber<List<CarPositionData>>(null) {
//                    @Override
//                    public void onNext(List<CarPositionData> carPositionDatas) {
//
//                    }
//                });

    }

}

