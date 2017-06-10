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
import rx.android.schedulers.AndroidSchedulers;
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


        Observable<RegeocodeAddress> observable = Observable.create((Observable.OnSubscribe<RegeocodeAddress>) subscriber -> {
            try {
                RegeocodeQuery query = new RegeocodeQuery(searchLatlonPoint, 100, GeocodeSearch.AMAP);
                RegeocodeAddress address = geocodeSearch.getFromLocation(query);
                if (address != null && !TextUtils.isEmpty(address.getFormatAddress())) {
                    subscriber.onNext(address);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Exception("当前address为null"));
                }
            } catch (AMapException e) {
                subscriber.onError(e);
            }


        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());


        return observable;


    }


    public static Observable<List<Tip>> getGeocodeAddressList(Context context, String key,
                                                              String city) {
        if (null == geocodeSearch) {
            geocodeSearch = new GeocodeSearch(context);
        }


        Observable<List<Tip>> observable = Observable.create((Observable.OnSubscribe<List<Tip>>) subscriber -> {
            try {


                InputtipsQuery inputquery = new InputtipsQuery(key, city);
                Inputtips inputTips = new Inputtips(context, inputquery);
                List<Tip> tips = inputTips.requestInputtips();

                if (tips != null && tips.size() != 0) {
                    subscriber.onNext(tips);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Exception("未能查询到数据"));
                }
            } catch (AMapException e) {
                subscriber.onError(e);
            }


        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());


        return observable;
    }


}
