package com.map.library.mvp.presenter;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DriveRouteResult;
import com.map.library.data.RouteData;
import com.map.library.help.AMapLocationHelper;
import com.map.library.mvp.IRouteNavi;
import com.map.library.rx.MapSubscriber;
import com.map.library.rx.RxMapManager;
import com.yisingle.baselibray.base.BasePresenter;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by jikun on 17/6/21.
 */

public class RouteNaviPresenterImpl extends BasePresenter<IRouteNavi.RouteView> implements IRouteNavi.RouteNaviPresenter {

    private AMapLocationHelper locationHelper;

    public RouteNaviPresenterImpl(Context context, @Nullable IRouteNavi.RouteView view) {
        super(view);
        locationHelper = new AMapLocationHelper(context);
    }

    @IntDef({Type.MAP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
        int MAP = 0;

    }


    /**
     * 定位点到目标点
     *
     * @param context
     * @param targetPoint
     */
    @Override
    public void drawSingleRoute(final Context context, final LatLonPoint targetPoint) {
        RxMapManager.getInstance().getLoctionObservable(locationHelper)
                .flatMap(new Func1<AMapLocation, Observable<List<RouteData>>>() {
                    @Override
                    public Observable<List<RouteData>> call(AMapLocation mapLocation) {

                        LatLonPoint locationPoint = new LatLonPoint(mapLocation.getLatitude(), mapLocation.getLongitude());
                        return getOneRouteObservable(context, locationPoint, targetPoint, RouteData.Type.CAR_TO_TARGET);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MapSubscriber<List<RouteData>>(mView, Type.MAP) {
                    @Override
                    public void onNext(List<RouteData> routeDatas) {

                        mView.ondrawRouteSuccess(routeDatas);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.ondrawRouteFailed(e);
                    }
                });


    }


    /**
     * 定位点到目标点  起点到终点  两条路径
     */
    @Override
    public void drawTwoRoute(final Context context, final LatLonPoint targetPoint, final LatLonPoint secondStartPoint, final LatLonPoint secondEndPoint) {
        RxMapManager.getInstance().getLoctionObservable(locationHelper)
                .flatMap(new Func1<AMapLocation, Observable<List<RouteData>>>() {
                    @Override
                    public Observable<List<RouteData>> call(AMapLocation mapLocation) {

                        LatLonPoint locationPoint = new LatLonPoint(mapLocation.getLatitude(), mapLocation.getLongitude());

                        Observable<List<RouteData>> currentObservable = getTwoRouteDataObservable(context, locationPoint, targetPoint, secondStartPoint, secondEndPoint);
                        return currentObservable;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MapSubscriber<List<RouteData>>(mView, Type.MAP) {
                    @Override
                    public void onNext(List<RouteData> routeDatas) {
                        mView.ondrawRouteSuccess(routeDatas);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.ondrawRouteFailed(e);
                    }
                });
    }

    @Override
    public void drawSingleRoute(Context context, LatLonPoint startPoint, LatLonPoint endPoint) {
        getOneRouteObservable(context, startPoint, endPoint, RouteData.Type.START_TO_END)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MapSubscriber<List<RouteData>>(mView, Type.MAP) {
                    @Override
                    public void onNext(List<RouteData> routeDatas) {
                        mView.ondrawRouteSuccess(routeDatas);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.ondrawRouteFailed(e);
                    }
                });

    }


    @Override
    public void drawTwoRoute(Context context, LatLonPoint firstStartPoint, LatLonPoint firstEndPoint, LatLonPoint secondStartPoint, LatLonPoint secondEndPoint) {
        getTwoRouteDataObservable(context, firstStartPoint, firstEndPoint, secondStartPoint, secondEndPoint)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MapSubscriber<List<RouteData>>(mView, Type.MAP) {
                    @Override
                    public void onNext(List<RouteData> routeDatas) {
                        mView.ondrawRouteSuccess(routeDatas);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.ondrawRouteFailed(e);
                    }
                });
    }


    private Observable<List<RouteData>> getOneRouteObservable(Context context, LatLonPoint startPoint, LatLonPoint endPoint, @RouteData.Type final int type) {


        Observable<List<RouteData>> observable = RxMapManager.getInstance().getRouteObservable(context, startPoint, endPoint)
                .map(new Func1<DriveRouteResult, List<RouteData>>() {
                    @Override
                    public List<RouteData> call(DriveRouteResult result) {
                        List<RouteData> routeDataList = new ArrayList<>();
                        RouteData normalData = RouteData.createRouteData(result, type);

                        routeDataList.add(normalData);
                        return routeDataList;
                    }
                }).subscribeOn(Schedulers.newThread());


        return observable;
    }

    private Observable<List<RouteData>> getTwoRouteDataObservable(Context context, LatLonPoint firstStartPoint, LatLonPoint firstEndPoint, LatLonPoint secondStartPoint, LatLonPoint secondEndPoint) {
        Observable<DriveRouteResult> fistObservable = RxMapManager.getInstance().getRouteObservable(context, firstStartPoint, firstEndPoint);

        Observable<DriveRouteResult> secondObservable = RxMapManager.getInstance().getRouteObservable(context, secondStartPoint, secondEndPoint);


        Observable<List<RouteData>> observable = Observable.zip(fistObservable, secondObservable, new Func2<DriveRouteResult, DriveRouteResult, List<RouteData>>() {
            @Override
            public List<RouteData> call(DriveRouteResult carResult, DriveRouteResult result) {
                List<RouteData> routeDataList = new ArrayList<>();
                RouteData carData = RouteData.createRouteData(carResult, RouteData.Type.CAR_TO_TARGET);
                RouteData normalData = RouteData.createRouteData(result, RouteData.Type.START_TO_END);
                routeDataList.add(carData);
                routeDataList.add(normalData);
                return routeDataList;
            }
        }).subscribeOn(Schedulers.newThread());

        return observable;
    }


    @Override
    public void onDestory() {
        super.onDestory();
        locationHelper.destroyLocation();
    }


}
