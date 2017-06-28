package com.yisingle.app.mvp.presenter;

import android.content.Context;
import android.support.annotation.IntDef;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.yisingle.app.base.BasePresenter;
import com.yisingle.app.data.CarPositionData;
import com.yisingle.app.data.ChoosePointData;
import com.yisingle.app.data.MapPointData;
import com.yisingle.app.data.OrderData;
import com.yisingle.app.http.ApiService;
import com.yisingle.app.http.RetrofitManager;
import com.yisingle.app.map.MapRxManager;
import com.yisingle.app.mvp.IFastCar;
import com.yisingle.app.rx.ApiSubscriber;
import com.yisingle.app.rx.RxUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jikun on 17/6/13.
 */

public class FastCarPresenter extends BasePresenter<IFastCar.FastCarView> implements IFastCar.FastCarPresenter {


    @IntDef({TYPE.REGEOCODE_ADDRESS, TYPE.SEND_ORDER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TYPE {

        int REGEOCODE_ADDRESS = 0;
        int SEND_ORDER = 1;


    }

    public FastCarPresenter(IFastCar.FastCarView view) {
        super(view);
    }

    @Override
    public void getRegeocodeAddress(Context context, LatLng latLng, int type) {

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
                mView.showLoading(type);
            }

            @Override
            public void onCompleted() {

                mView.dismissLoading(type);
            }

            @Override
            public void onError(Throwable e) {

                mView.dismissLoading(type);
                mView.onError(type);
            }

            @Override
            public void onNext(ChoosePointData choosePointData) {

                mView.onAddressSuccess(choosePointData);

            }
        });

    }

    @Override
    public void sendOrder(String phoneNum, MapPointData startMapPointData, MapPointData endMapPointData, int type) {
        Map<String, String> params = new HashMap<>();
        params.put("phoneNum", phoneNum);
        params.put("startLatitude", String.valueOf(startMapPointData.getLatLng().latitude));
        params.put("startLongitude", String.valueOf(startMapPointData.getLatLng().longitude));
        params.put("startPlaceName", startMapPointData.getText());
        params.put("endLatitude", String.valueOf(endMapPointData.getLatLng().latitude));
        params.put("endLongitude", String.valueOf(endMapPointData.getLatLng().longitude));
        params.put("endPlaceName", endMapPointData.getText());

        RetrofitManager.getInstance().createService(ApiService.class).sendOrderData(params)
                .compose(RxUtils.apiChildTransformer())
                .subscribe(new ApiSubscriber<OrderData>(mView,type) {
                    @Override
                    public void onNext(OrderData data) {
                        mView.onSendOrderSuccess(data);
                    }
                });


    }


}

