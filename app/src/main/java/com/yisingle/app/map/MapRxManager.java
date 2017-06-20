package com.yisingle.app.map;

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;

import java.util.List;

import rx.Observable;
import rx.observables.SyncOnSubscribe;
import rx.schedulers.Schedulers;

/**
 * Created by jikun on 17/5/15.
 */


public class MapRxManager {

    public static GeocodeSearch geocodeSearch;


    /**
     * 获取逆地理路径的Rx的Observable
     * 功能，根据坐标获取选中的位置名称
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return Observable返回Rxjava的Observable
     */

    public static Observable<RegeocodeAddress> getRegeocodeAddressObservable(Context context, double latitude, double longitude) {


        if (null == geocodeSearch) {
            geocodeSearch = new GeocodeSearch(context);
        }


        LatLonPoint searchLatlonPoint = new LatLonPoint(latitude, longitude);

        SyncOnSubscribe<String, RegeocodeAddress> syncOnSubscribe = SyncOnSubscribe.createSingleState(() -> "", (s, observer) -> {
            try {
                RegeocodeQuery query = new RegeocodeQuery(searchLatlonPoint, 100, GeocodeSearch.AMAP);
                RegeocodeAddress address = geocodeSearch.getFromLocation(query);
                if (address != null && !TextUtils.isEmpty(address.getFormatAddress())) {
                    observer.onNext(address);
                    observer.onCompleted();
                } else {
                    observer.onError(new Exception("当前address为null"));
                }
            } catch (AMapException e) {
                observer.onError(e);
            }

        });


        Observable<RegeocodeAddress> observable = Observable.create(syncOnSubscribe).subscribeOn(Schedulers.computation());


        return observable;


    }


    public static Observable<List<Tip>> getGeocodeAddressList(Context context, String key,
                                                              String city) {
        if (null == geocodeSearch) {
            geocodeSearch = new GeocodeSearch(context);
        }

        SyncOnSubscribe<String, List<Tip>> syncOnSubscribe = SyncOnSubscribe
                .createSingleState(() -> "", (s, observer) -> {
                    try {

                        InputtipsQuery inputquery = new InputtipsQuery(key, city);
                        Inputtips inputTips = new Inputtips(context, inputquery);
                        List<Tip> tips = inputTips.requestInputtips();

                        if (tips != null && tips.size() != 0) {
                            observer.onNext(tips);
                            observer.onCompleted();
                        } else {
                            observer.onError(new Exception("未能查询到数据"));
                        }
                    } catch (AMapException e) {
                        observer.onError(e);
                    }

                });

        // Observable.create(SyncOnSubscribe.createSingleState(f0,a2));
        Observable<List<Tip>> observable =
                Observable.create(syncOnSubscribe).subscribeOn(Schedulers.computation());


        return observable;
    }


}
