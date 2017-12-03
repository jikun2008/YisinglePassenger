package com.map.library.rx;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.map.library.help.AMapLocationHelper;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action2;
import rx.functions.Action3;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.observables.AsyncOnSubscribe;
import rx.observables.SyncOnSubscribe;
import rx.schedulers.Schedulers;

/**
 * Created by jikun on 17/6/20.
 */

public class RxMapManager {
    public static GeocodeSearch geocodeSearch;

    // 定义一个私有构造方法
    private RxMapManager() {

    }

    //定义一个静态私有变量(不初始化，不使用final关键字，使用volatile保证了多线程访问时instance变量的可见性，避免了instance初始化时其他变量属性还没赋值完时，被另外线程调用)
    private static volatile RxMapManager instance;

    //定义一个共有的静态方法，返回该类型实例
    public static RxMapManager getInstance() {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (instance == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (RxMapManager.class) {
                //未初始化，则初始instance变量
                if (instance == null) {
                    instance = new RxMapManager();
                }
            }
        }
        return instance;
    }


    /**
     * 获取路径规划的Observable类
     *
     * @param context
     * @param startPoint//开始位置
     * @param endPoint//目标位置
     * @return
     */
    public Observable<DriveRouteResult> getRouteObservable(final Context context, final LatLonPoint startPoint, final LatLonPoint endPoint) {


        Observable<DriveRouteResult> observable = Observable.create(new Observable.OnSubscribe<DriveRouteResult>() {
            @Override
            public void call(Subscriber<? super DriveRouteResult> subscriber) {
                try {

                    RouteSearch routeSearch = new RouteSearch(context);
                    int mode = RouteSearch.DRIVING_SINGLE_DEFAULT;

                    RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                            startPoint, endPoint);
                    RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null,
                            null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
                    DriveRouteResult driveRouteResult = routeSearch.calculateDriveRoute(query);// 同步路径规划驾车模式查询
                    Log.e("测试代码", "测试代码getRouteObservable---driveRouteResult");
                    subscriber.onNext(driveRouteResult);
                    subscriber.onCompleted();
                } catch (AMapException e) {
                    e.printStackTrace();
                    Log.e("测试代码", "测试代码getRouteObservable---" + e.toString());
                    subscriber.onError(e);
                }
            }
        }).filter(new Func1<DriveRouteResult, Boolean>() {
            @Override
            public Boolean call(DriveRouteResult driveRouteResult) {
                return driveRouteResult != null && driveRouteResult.getPaths() != null && driveRouteResult.getPaths().size() > 0;
            }
        })
                .subscribeOn(Schedulers.newThread());


        return observable;
    }


    /**
     * 封装异步操作为Observable  请注意  AsyncOnSubscribe是用来封装异步请求为Observable
     *
     * @param locationHelper
     * @return
     */
    public Observable<AMapLocation> getLoctionObservable(final AMapLocationHelper locationHelper) {

        AsyncOnSubscribe<String, AMapLocation> asyncOnSubscribe = AsyncOnSubscribe.createSingleState(new Func0<String>() {
            @Override
            public String call() {
                return "";
            }
        }, new Action3<String, Long, Observer<Observable<? extends AMapLocation>>>() {
            @Override
            public void call(String s, Long aLong, Observer<Observable<? extends AMapLocation>> observableObserver) {

                Log.e("测试代码", "测试代码getRouteObservable+onLocation" + "start-start-start-start");
                observableObserver.onNext(getLoctionHelperObservable(locationHelper));
                observableObserver.onCompleted();
            }
        });

        return Observable.create(asyncOnSubscribe).subscribeOn(Schedulers.newThread());
    }

    private Observable<AMapLocation> getLoctionHelperObservable(final AMapLocationHelper locationHelper) {

        Observable<AMapLocation> locationObservable = Observable.create(new Observable.OnSubscribe<AMapLocation>() {
            @Override
            public void call(final Subscriber<? super AMapLocation> subscriber) {
                locationHelper.startSingleLocateNoStop(new AMapLocationHelper.OnLocationGetListener() {
                    @Override
                    public void onLocationGetSuccess(AMapLocation loc) {
                        subscriber.onNext(loc);
                        subscriber.onCompleted();
                        Log.e("测试代码", "测试代码getRouteObservable+onLocationGetSuccess");
                    }

                    @Override
                    public void onLocationGetFail(AMapLocation loc) {
                        Log.e("测试代码", "测试代码getRouteObservable+onLocationGetFail");
                        String errorInfo = "高德定位报错";
                        if (null != loc && null != loc.getErrorInfo()) {
                            errorInfo = loc.getErrorCode() + loc.getErrorInfo();
                        }

                        subscriber.onError(new Exception(errorInfo));
                    }
                });
            }
        });

        return locationObservable;
    }


    /**
     * 获取逆地理路径的Rx的Observable
     * 功能，根据坐标获取选中的位置名称
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return Observable返回Rxjava的Observable
     */

    public  Observable<RegeocodeAddress> getRegeocodeAddressObservable(Context context, double latitude, double longitude) {


        if (null == geocodeSearch) {
            geocodeSearch = new GeocodeSearch(context);
        }


        final LatLonPoint searchLatlonPoint = new LatLonPoint(latitude, longitude);


        SyncOnSubscribe<String, RegeocodeAddress> syncOnSubscribe = SyncOnSubscribe.createSingleState(new Func0<String>() {
            @Override
            public String call() {
                return "";
            }
        }, new Action2<String, Observer<? super RegeocodeAddress>>() {
            @Override
            public void call(String s, Observer<? super RegeocodeAddress> observer) {
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
            }
        });


        Observable<RegeocodeAddress> observable = Observable.create(syncOnSubscribe).subscribeOn(Schedulers.computation());


        return observable;


    }


    public  Observable<List<Tip>> getGeocodeAddressList(final Context context, final String key,
                                                              final String city) {
        if (null == geocodeSearch) {
            geocodeSearch = new GeocodeSearch(context);
        }
        SyncOnSubscribe<String, List<Tip>> syncOnSubscribe = SyncOnSubscribe
                .createSingleState(new Func0<String>() {
                    @Override
                    public String call() {
                        return "";
                    }
                }, new Action2<String, Observer<? super List<Tip>>>() {
                    @Override
                    public void call(String s, Observer<? super List<Tip>> observer) {
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


                    }
                });

        // Observable.create(SyncOnSubscribe.createSingleState(f0,a2));
        Observable<List<Tip>> observable =
                Observable.create(syncOnSubscribe).subscribeOn(Schedulers.computation());


        return observable;
    }

}
