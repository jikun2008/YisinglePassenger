package com.yisingle.app.map.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.IntDef;
import android.text.SpannableStringBuilder;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.blankj.utilcode.util.SpanUtils;
import com.map.library.view.base.BaseMapMarkerView;
import com.map.library.view.base.BaseMarkerData;
import com.map.library.view.base.BaseWindowData;
import com.yisingle.app.R;
import com.yisingle.app.map.utils.MarkerBuilder;
import com.yisingle.app.view.CircleTimeCountDownVIew;
import com.yisingle.baselibray.baseadapter.viewholder.MapInfoWindowViewHolder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import rx.Subscription;

/**
 * Created by jikun on 17/6/28.
 */

public class CarMapMarkerView extends BaseMapMarkerView<CarMapMarkerView.CarMapMarkerData, CarMapMarkerView.CarMapWindowData> {


    Subscription timesubscription;

    public CarMapMarkerView(AMap aMap, Context context) {
        super(aMap, context);
    }

    @Override
    protected Marker buildMarker(CarMapMarkerData markerData) {
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                markerData.getDrawableId());
        Marker marker = MarkerBuilder.getStartMarkerToMapView(markerData.getLatLng(),
                bitmap, getMap());
        return marker;
    }

    @Override
    protected void addMarkSuccess(CarMapMarkerData markerData) {

    }

    CircleTimeCountDownVIew countDownVIew;

    public void initMarkInfoWindowAdapter(CarMapWindowData carMapWindowData) {
        initMarkInfoWindowAdapter(carMapWindowData, R.layout.map_car_order_info_window, new InfoWindowListener<CarMapWindowData>() {
            @Override
            public void bindData(MapInfoWindowViewHolder viewHolder, CarMapWindowData data) {
                countDownVIew = viewHolder.get(R.id.view_countDownView);
                switch (data.getType()) {
                    case CarMapWindowData.Type.DrivingToStart:
                        viewHolder.setVisibility(R.id.ll_driver_start, View.VISIBLE);
                        viewHolder.setVisibility(R.id.rl_driver_trip, View.GONE);
                        SpannableStringBuilder discont = new SpanUtils()
                                .append("距您")
                                .append(data.getData().getDistance() + data.getData().getTime()).setForegroundColor(getContext().getResources().getColor(R.color.orange_text))
                                .create();
                        viewHolder.setText(R.id.tv_info, discont);


                        countDownVIew.setVisibility(View.GONE);
                        break;

                    case CarMapWindowData.Type.DrivedAtSart:
                        viewHolder.setVisibility(R.id.ll_driver_start, View.VISIBLE);
                        viewHolder.setVisibility(R.id.rl_driver_trip, View.GONE);

                        String text = "车辆已到达";
                        viewHolder.setText(R.id.tv_info, text);
                        countDownVIew.setVisibility(View.VISIBLE);

                        countDownVIew.startCoutDown();
                        break;
                    case CarMapWindowData.Type.DriveAtTrip:
                        viewHolder.setVisibility(R.id.ll_driver_start, View.GONE);
                        viewHolder.setVisibility(R.id.rl_driver_trip, View.VISIBLE);


                        SpannableStringBuilder time = new SpanUtils()
                                .append("预计行驶")
                                .append(data.getData().getTime()).setForegroundColor(getContext().getResources().getColor(R.color.orange_text))
                                .create();
                        //预计行驶11分钟
                        viewHolder.setText(R.id.tv_time, time);

                        SpannableStringBuilder ditance = new SpanUtils()
                                .append("距离终点")
                                .append(data.getData().getDistance()).setForegroundColor(getContext().getResources().getColor(R.color.orange_text))
                                .create();
                        //距离终点3.9公里
                        viewHolder.setText(R.id.tv_distance, ditance);

                        //1.14元
                        viewHolder.setText(R.id.tv_price, data.getData().getPrice() + "元");

                        break;
                    default:
                        break;
                }


            }
        });

    }

    public static class CarMapMarkerData extends BaseMarkerData {

        public CarMapMarkerData(LatLng latLng, int drawableId) {
            this.latLng = latLng;
            this.drawableId = drawableId;
        }
    }

    public static class CarMapWindowData extends BaseWindowData {


        private int type = Type.DrivingToStart;

        private long currentTime = 0;


        private TripData data;

        public TripData getData() {
            return data;
        }

        public void setData(TripData data) {
            this.data = data;
        }

        public static class TripData {

            public TripData(String distance, String time, String price) {
                this.distance = distance;
                this.time = time;
                this.price = price;
            }

            private String distance;

            private String time;

            private String price;

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }


        public void setType(int type) {
            this.type = type;
        }

        public long getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(long currentTime) {
            this.currentTime = currentTime;
        }

        public
        @Type
        int getType() {
            return type;
        }

        //添加支持注解的依赖到你的项目中，需要在build.gradle文件中的依赖块中添加：
        //dependencies { compile 'com.android.support:support-annotations:24.2.0' }
        @IntDef({Type.DrivingToStart, Type.DrivedAtSart, Type.DriveAtTrip})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Type {


            int DrivingToStart = 0;//司机在前往乘客起点途中
            int DrivedAtSart = 1;//司机已经到达
            int DriveAtTrip = 2;//在行程中


        }

        private CarMapWindowData(boolean isshow, @Type int type, TripData tripData, long time) {
            this.isShowWindow = isshow;
            this.type = type;
            this.currentTime = time;
            this.data = tripData;
        }

        public static CarMapWindowData createSimpleData(boolean isshow, @Type int type) {

            TripData tripData = new TripData("", "", "");
            CarMapWindowData carMapWindowData = new CarMapWindowData(isshow, type, tripData, 0);
            return carMapWindowData;
        }

        public static CarMapWindowData createTripata(boolean isshow, @Type int type, TripData tripData) {
            CarMapWindowData carMapWindowData = new CarMapWindowData(isshow, type, tripData, 0);
            return carMapWindowData;
        }

    }

    public void moveToCamera() {
        float zoom = 17;//设置缩放级别
        if (null != getMap() && null != getMarker()) {
            getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(getMarker().getPosition(), zoom));//zoom - 缩放级别，[3-20]。
        }
    }


    @Override
    public void removeView() {
        super.removeView();
        if (countDownVIew != null) {
            countDownVIew.stopCountTime();
        }

    }
}
