package com.map.library.mvp;

import android.content.Context;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
import com.map.library.data.RouteData;
import com.yisingle.baselibray.base.BaseView;


import java.util.List;

/**
 * Created by jikun on 17/6/21.
 */

public interface IRouteNavi {

    interface RouteView extends BaseView {
        void ondrawRouteSuccess(List<RouteData> routeResultList);

        void ondrawRouteFailed(Throwable e);

    }

    interface RouteNaviPresenter {


        //定位点到目标点绘制单一路线
        void drawSingleRoute(Context context, LatLonPoint targetPoint);

        //绘制两条路线一条定位点到目标点。
        void drawTwoRoute(Context context, LatLonPoint targetPoint, LatLonPoint secondStartPoint, LatLonPoint secondEndPoint);

        //已经知道两个点绘制单一路线
        void drawSingleRoute(Context context, LatLonPoint startPoint, LatLonPoint endPoint);

        //绘制两条路线
        void drawTwoRoute(Context context, LatLonPoint firstStartPoint, LatLonPoint firstEndPoint, LatLonPoint secondStartPoint, LatLonPoint secondEndPoint);




    }
}
