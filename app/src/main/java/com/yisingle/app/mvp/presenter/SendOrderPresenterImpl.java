package com.yisingle.app.mvp.presenter;

import android.content.Context;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DriveRouteResult;
import com.map.library.data.RouteData;
import com.map.library.mvp.presenter.RouteNaviPresenterImpl;
import com.map.library.rx.MapSubscriber;
import com.map.library.rx.RxMapManager;
import com.yisingle.app.mvp.ISendOrder;
import com.yisingle.baselibray.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by jikun on 17/8/4.
 */

public class SendOrderPresenterImpl extends BasePresenter<ISendOrder.SendOrderView> implements ISendOrder.SendOrderPresenter {
    public SendOrderPresenterImpl(ISendOrder.SendOrderView view) {
        super(view);
    }

    @Override
    public void drawSingleRoute(Context context, LatLonPoint startPoint, LatLonPoint endPoint, boolean ishowLoading) {
        getOneRouteObservable(context, startPoint, endPoint, RouteData.Type.START_TO_END)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MapSubscriber<List<RouteData>>(mView, 0, ishowLoading) {
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
    public void drawSingleRoute(Context context, LatLng startPoint, LatLng endPoint, boolean ishowLoading) {
        LatLonPoint startLatLonPoint = new LatLonPoint(startPoint.latitude, startPoint.longitude);
        LatLonPoint endLatLonPoint = new LatLonPoint(endPoint.latitude, endPoint.longitude);
        drawSingleRoute(context, startLatLonPoint, endLatLonPoint, ishowLoading);
    }

    private Observable<List<RouteData>> getOneRouteObservable(Context context, LatLonPoint startPoint, LatLonPoint endPoint, int type) {


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
}
