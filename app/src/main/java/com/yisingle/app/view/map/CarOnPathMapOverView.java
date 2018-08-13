package com.yisingle.app.view.map;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.text.SpannableStringBuilder;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.blankj.utilcode.util.SpanUtils;
import com.map.library.DistanceUtils;
import com.yisingle.amapview.lib.base.view.marker.BaseMarkerView;
import com.yisingle.amapview.lib.utils.move.MovePathPlanningUtils;
import com.yisingle.amapview.lib.view.CarMoveOnPathPlaningView;
import com.yisingle.amapview.lib.view.PathPlaningView;
import com.yisingle.amapview.lib.view.PointMarkerView;
import com.yisingle.amapview.lib.viewholder.MapInfoWindowViewHolder;
import com.yisingle.app.R;
import com.yisingle.baselibray.utils.TimeDisUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jikun
 * Created by jikun on 2018/8/10.
 */
public class CarOnPathMapOverView {


    private Context context;

    private AMap aMap;

    private OnListener listener;

    @Type
    int type = Type.Time_Distance;

    private CarMoveOnPathPlaningView<MovePathPlanningUtils.DistanceDurationData, String, String> carMoveOnPathPlaningView;


    public CarOnPathMapOverView(Context context, AMap aMap) {

        this.context = context;
        this.aMap = aMap;
        carMoveOnPathPlaningView = new CarMoveOnPathPlaningView.Builder(context, aMap)

                .setPathPlaningViewBuilder(new PathPlaningView.Builder(context, aMap)
                        .setEndMarkBuilder(new PointMarkerView.Builder(context, aMap).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_start))))
                .create();

        carMoveOnPathPlaningView.setListener(new MovePathPlanningUtils.OnDistanceDurationListener() {
            @Override
            public void onDataCallBack(MovePathPlanningUtils.DistanceDurationData data) {
                carMoveOnPathPlaningView.showMoveCarInfoWindow(data);
            }

            @Override
            public void onDriverRouteSuccess() {
                if (null != listener) {
                    listener.onDriverRouteSuccess();
                }


            }
        });
        carMoveOnPathPlaningView.bingMoveCarInfoWindowView(new BaseMarkerView.BaseInfoWindowView<MovePathPlanningUtils.DistanceDurationData>(R.layout.map_driver_to_start_car_info_window, null) {
            @Override
            public void bindData(MapInfoWindowViewHolder viewHolder, MovePathPlanningUtils.DistanceDurationData data) {


                switch (type) {
                    case Type.Time_Distance:
                        //将预估的时间转换为显示的时间
                        String showtime = TimeDisUtils.changeSectoMin((int) data.getDuration()) + "分钟";

                        float distance = data.getDistance();
                        if (distance < 500) {
                            distance = 500;
                        }
                        String distanceInfo = DistanceUtils.getDistance((int) distance);
                        SpannableStringBuilder discont = new SpanUtils()
                                .append("距您")
                                .append(distanceInfo + "----" + showtime + "")
                                .setForegroundColor(context.getResources().getColor(R.color.orange_text))
                                .create();
                        viewHolder.setText(R.id.tv_driver_info, discont);
                        break;
                    case Type.Already_Arrived:
                        viewHolder.setText(R.id.tv_driver_info, "司机已到达");
                        break;
                    default:
                        break;
                }

            }


        });

    }

    public void removeFromMap() {

        carMoveOnPathPlaningView.removeFromMap();

    }


    public void drawStart(LatLng driverLatLng, LatLng latLng, String text) {

        type = Type.Time_Distance;
        List<LatLng> list = new ArrayList<>();
        list.add(driverLatLng);


        carMoveOnPathPlaningView.getPathPlaningView().getEndPointMarkerView().setText(text);
        carMoveOnPathPlaningView.startMove(list, latLng);
        Log.e("测试代码", "测试代码startMove" + "-------drawStart");

    }

    public void drawAlreadyArrived(LatLng driverLatLng, LatLng latLng, String text) {

        type = Type.Already_Arrived;
        List<LatLng> list = new ArrayList<>();
        list.add(driverLatLng);


        carMoveOnPathPlaningView.getPathPlaningView().getEndPointMarkerView().setText(text);
        carMoveOnPathPlaningView.startMove(list, latLng);
        Log.e("测试代码", "测试代码startMove" + "-------drawAlreadyArrived");

    }

    public void drawEnd(LatLng driverLatLng, LatLng latLng, String text) {

        type = Type.Time_Distance;
        List<LatLng> list = new ArrayList<>();
        list.add(driverLatLng);


        carMoveOnPathPlaningView.getPathPlaningView().getEndPointMarkerView().setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_end));
        carMoveOnPathPlaningView.getPathPlaningView().getEndPointMarkerView().setText(text);
        carMoveOnPathPlaningView.startMove(list, latLng);
        Log.e("测试代码", "测试代码startMove" + "-------drawEnd");

    }


    //添加支持注解的依赖到你的项目中，需要在build.gradle文件中的依赖块中添加：
    //dependencies { compile 'com.android.support:support-annotations:24.2.0' }
    @IntDef({Type.Time_Distance, Type.Already_Arrived})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {

        int Time_Distance = 0;
        int Already_Arrived = 1;


    }

    /**
     * 在导航的地图MapView上移动视角
     */
    public void moveToCamera(LatLng start, LatLng end, Rect rect) {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();


        builder.include(start);
        builder.include(end);
        LatLngBounds latLngBounds = builder.build();
        // newLatLngBoundsRect(LatLngBounds latlngbounds, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom)
        //newLatLngBoundsRect(LatLngBounds latlngbounds,
        //int paddingLeft,设置经纬度范围和mapView左边缘的空隙。
        //int paddingRight,设置经纬度范围和mapView右边缘的空隙
        //int paddingTop,设置经纬度范围和mapView上边缘的空隙。
        //int paddingBottom)设置经纬度范围和mapView下边缘的空隙。
        if (null != getAmap()) {
            getAmap().animateCamera(CameraUpdateFactory.newLatLngBoundsRect(latLngBounds, rect.left, rect.right, rect.top, rect.bottom));
        }


    }

    public AMap getAmap() {
        return aMap;
    }

    public interface OnListener {
        void onDriverRouteSuccess();

    }


    public void setListener(OnListener listener) {
        this.listener = listener;
    }

    public CarMoveOnPathPlaningView<MovePathPlanningUtils.DistanceDurationData, String, String> getCarMoveOnPathPlaningView() {
        return carMoveOnPathPlaningView;
    }
}
