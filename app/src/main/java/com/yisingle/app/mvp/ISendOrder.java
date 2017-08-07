package com.yisingle.app.mvp;

import android.content.Context;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.map.library.data.RouteData;
import com.yisingle.baselibray.base.BaseView;

import java.util.List;

/**
 * Created by jikun on 17/8/4.
 */

public interface ISendOrder {

     interface SendOrderView extends BaseView {

        void ondrawRouteSuccess(List<RouteData> routeResultList);

        void ondrawRouteFailed(Throwable e);
    }

    interface SendOrderPresenter {
        public void drawSingleRoute(Context context, LatLonPoint startPoint, LatLonPoint endPoint,boolean ishowLoading);


         void drawSingleRoute(Context context, LatLng startPoint, LatLng endPoint,boolean ishowLoading);

    }
}
