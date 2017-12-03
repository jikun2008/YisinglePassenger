package com.map.library.view;

import android.content.Context;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.library.R;
import com.map.library.DistanceUtils;
import com.map.library.view.marker.PointMapMarkerView;
import com.map.library.view.polyline.MapPolylineView;
import com.yisingle.baselibray.utils.TimeDisUtils;


/**
 * Created by jikun on 17/6/21.
 */

/**
 * 路线规划的只显示路线规划
 */
public class MapRouteView {

    private AMap map;

    private Context context;

    private PointMapMarkerView carMarkerView;

    private PointMapMarkerView targetMarkerView;

    private MapPolylineView carToTargetpolylineView;

    private PointMapMarkerView startMarkerView;

    private PointMapMarkerView endMarkerView;

    private MapPolylineView startToEndpolylineView;


    public MapRouteView(AMap aMap, Context context) {
        this.map = aMap;
        this.context = context;
        carMarkerView = new PointMapMarkerView(aMap, context);
        targetMarkerView = new PointMapMarkerView(aMap, context);
        carToTargetpolylineView = new MapPolylineView(context, aMap);

        startMarkerView = new PointMapMarkerView(aMap, context);
        endMarkerView = new PointMapMarkerView(aMap, context);
        startToEndpolylineView = new MapPolylineView(context, aMap);


    }


    public void addCarRoute(DriveRouteResult result) {
        if (result != null && result.getPaths() != null && result.getPaths().size() > 0) {


            DrivePath drivePath = result.getPaths()
                    .get(0);
            LatLng latLng = new LatLng(result.getStartPos().getLatitude(), result.getStartPos().getLongitude());
            PointMapMarkerView.PointMarkerData data = PointMapMarkerView.PointMarkerData.createData(R.mipmap.icon_car, latLng, true);
            carMarkerView.addView(data);

            String showtime = TimeDisUtils.changeSectoMin((int) drivePath.getDuration()) + "分钟";    //将预估的时间转换为显示的时间

            float distance = drivePath.getDistance();
            if (distance < 500) {
                distance = 500;
            }
            String distanceInfo = DistanceUtils.getDistance((int) distance);
            carMarkerView.initMarkInfoWindowAdapter(PointMapMarkerView.PointWindowData.createIsshowData(showtime, distanceInfo));

            LatLng startLatLng = new LatLng(result.getTargetPos().getLatitude(), result.getTargetPos().getLongitude());
            PointMapMarkerView.PointMarkerData startData = PointMapMarkerView.PointMarkerData.createData(R.mipmap.icon_start, startLatLng, false);
            targetMarkerView.addView(startData);


            carToTargetpolylineView.addLine(result.getPaths().get(0));
        }
    }

    public void addStartToEndRoute(DriveRouteResult result) {
        if (result != null && result.getPaths() != null && result.getPaths().size() > 0) {
            DrivePath drivePath = result.getPaths()
                    .get(0);
            if (result.getStartPos() == null) {
                Log.e("测试代码", "测试代码result.getStartPos()==null");
                Log.e("测试代码", "测试代码result.getStartPos()==null");
            }

            LatLng startLatLng = new LatLng(result.getStartPos().getLatitude(), result.getStartPos().getLongitude());
            PointMapMarkerView.PointMarkerData startData = PointMapMarkerView.PointMarkerData.createData(R.mipmap.icon_start, startLatLng, false);
            startMarkerView.addView(startData);

            LatLng endLatLng = new LatLng(result.getTargetPos().getLatitude(), result.getTargetPos().getLongitude());
            PointMapMarkerView.PointMarkerData endData = PointMapMarkerView.PointMarkerData.createData(R.mipmap.icon_end, endLatLng, false);
            endMarkerView.addView(endData);
            startToEndpolylineView.addLine(drivePath);
        }

    }

    public void removeRoute() {

        if (null != carMarkerView) {
            carMarkerView.removeView();
        }
        if (null != targetMarkerView) {
            targetMarkerView.removeView();
        }


        if (null != startMarkerView) {
            startMarkerView.removeView();
        }
        if (null != endMarkerView) {
            endMarkerView.removeView();
        }
        if (null != startToEndpolylineView) {
            startToEndpolylineView.removeLine();
        }
        if (null != carToTargetpolylineView) {
            carToTargetpolylineView.removeLine();
        }


    }

}
