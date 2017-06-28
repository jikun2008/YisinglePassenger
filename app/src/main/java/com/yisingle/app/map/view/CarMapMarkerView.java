package com.yisingle.app.map.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableStringBuilder;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.yisingle.app.R;
import com.yisingle.app.map.utils.MarkerBuilder;
import com.yisingle.app.utils.SpannableStringUtils;
import com.yisingle.baselibray.baseadapter.viewholder.MapInfoWindowViewHolder;

/**
 * Created by jikun on 17/6/28.
 */

public class CarMapMarkerView extends BaseMapMarkerView<CarMapMarkerView.CarMapMarkerData, CarMapMarkerView.CarMapWindowData> {


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

    public void initMarkInfoWindowAdapter() {
        initMarkInfoWindowAdapter(new CarMapWindowData(), R.layout.map_car_order_info_window, new InfoWindowListener<CarMapWindowData>() {
            @Override
            public void bindData(MapInfoWindowViewHolder viewHolder, CarMapWindowData data) {

                SpannableStringBuilder discont = new SpannableStringUtils.Builder()
                        .append("距您")
                        .append("2.6公里 8分钟").setForegroundColor(getContext().getResources().getColor(R.color.orange_text))
                        .create();
                viewHolder.setText(R.id.tv_info, discont);

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

        public CarMapWindowData() {
            this.isShowWindow = true;
        }

    }

    public void moveToCamera() {
        float zoom = 17;//设置缩放级别
        if (null != getMap() && null != getMarker()) {
            getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(getMarker().getPosition(), zoom));//zoom - 缩放级别，[3-20]。
        }
    }

}
