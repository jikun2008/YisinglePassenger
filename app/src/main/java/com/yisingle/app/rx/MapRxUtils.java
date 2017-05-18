package com.yisingle.app.rx;

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by jikun on 17/5/15.
 */


public class MapRxUtils {

    public static GeocodeSearch geocodeSearch;


    /**
     * 获取逆地理路径的Rx的Observable
     * 功能，根据坐标获取选中的位置名称
     *
     * @param latitude 纬度
     * @param longitude 经度
     * @return  Observable返回Rxjava的Observable
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


        }).subscribeOn(Schedulers.computation());


        return observable;


    }


}
