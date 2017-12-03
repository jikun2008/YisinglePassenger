package com.map.library.view.marker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.text.SpannableStringBuilder;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

import com.amap.library.R;
import com.blankj.utilcode.util.SpanUtils;
import com.map.library.view.MarkerBuilder;
import com.map.library.view.base.BaseMapMarkerView;
import com.map.library.view.base.BaseMarkerData;
import com.map.library.view.base.BaseWindowData;
import com.yisingle.baselibray.baseadapter.viewholder.MapInfoWindowViewHolder;


/**
 * Created by jikun on 17/6/11.
 */

public class PointMapMarkerView extends BaseMapMarkerView<PointMapMarkerView.PointMarkerData, PointMapMarkerView.PointWindowData> {


    public PointMapMarkerView(AMap aMap, Context context) {
        super(aMap, context);

    }


    @Override
    protected Marker buildMarker(PointMarkerData markerData) {

        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                markerData.getDrawableId());
        Marker marker = MarkerBuilder.getMarkerToMapView(markerData.getLatLng(),
                bitmap, getMap());
        return marker;
    }

    @Override
    protected void addMarkSuccess(PointMarkerData markerData) {

    }

    @Override
    public void removeView() {
        super.removeView();

    }


    public void initMarkInfoWindowAdapter(PointWindowData data) {
        initMarkInfoWindowAdapter(data, R.layout.map_library_car_order_info_window, new InfoWindowListener<PointWindowData>() {
            @Override
            public void bindData(MapInfoWindowViewHolder viewHolder, PointWindowData windowData) {


                SpannableStringBuilder discont = new SpanUtils()
                        .append("距离乘客起点")
                        .append(windowData.getDistance() + "").setForegroundColor(getContext().getResources().getColor(R.color.map_orange))
                        .append(",需要")
                        .append(windowData.getTime() + "").setForegroundColor(getContext().getResources().getColor(R.color.map_orange))
                        .create();
                viewHolder.setText(R.id.tv_amap_info, discont);


            }
        });
    }


    public static class PointMarkerData extends BaseMarkerData {


        private PointMarkerData() {

        }


        public static PointMarkerData createData(@DrawableRes int drawableId, LatLng latLng, boolean ishowInfoWindow) {
            PointMarkerData markerData = new PointMarkerData();
            markerData.setLatLng(latLng);
            markerData.setDrawableId(drawableId);
            return markerData;
        }
    }

    public static class PointWindowData extends BaseWindowData {

        private String distance;

        private String time;

        private PointWindowData() {


        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public static PointWindowData createData(String time, String distance) {
            PointWindowData data = new PointWindowData();
            data.setTime(time);
            data.setDistance(distance);
            return data;
        }

        public static PointWindowData createIsshowData(String time, String distance) {
            PointWindowData data = new PointWindowData();
            data.setTime(time);
            data.setDistance(distance);
            data.setShowWindow(true);
            return data;
        }
    }
}
