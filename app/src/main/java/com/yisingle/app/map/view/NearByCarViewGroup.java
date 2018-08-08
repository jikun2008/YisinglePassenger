package com.yisingle.app.map.view;

import android.content.Context;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.yisingle.amapview.lib.utils.TwoArrayUtils;
import com.yisingle.amapview.lib.view.PointMarkerView;
import com.yisingle.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jikun
 * Created by jikun on 2018/6/25.
 */
public class NearByCarViewGroup {

    private Context context;

    private AMap amap;


    private List<PointMarkerView> carPointMarkerViewList = new ArrayList<>();

    private List<List<LatLng>> dataList = new ArrayList<>();

    public NearByCarViewGroup(Context context, AMap amap) {
        this.amap = amap;
        this.context = context;
    }

    public void setLatLngList(List<List<LatLng>> list) {
        this.dataList = list;

        TwoArrayUtils.looperCompare(carPointMarkerViewList, dataList, new TwoArrayUtils.Listener<List<PointMarkerView>, List<List<LatLng>>>() {
            @Override
            public void onOneMore(List<PointMarkerView> more, List<PointMarkerView> remain) {
                if (null != carPointMarkerViewList && null != dataList) {
                    for (PointMarkerView view : more) {
                        view.destory();
                    }

                    carPointMarkerViewList = remain;

                    beginMove();
                }

            }

            @Override
            public void onTwoMore(List<List<LatLng>> more, List<List<LatLng>> remain) {

                for (List<LatLng> data : more) {
                    carPointMarkerViewList.add(produce());
                }

                beginMove();

            }

            @Override
            public void onSizeEqual() {

                beginMove();

            }
        });
    }

    private void beginMove() {
        for (int i = 0; i < carPointMarkerViewList.size(); i++) {

            if (i < dataList.size()) {
                List<LatLng> data = dataList.get(i);
                carPointMarkerViewList.get(i).startMove(data, false);
                carPointMarkerViewList.get(i).setVisible(true);
            }


        }
    }


    public void hide() {

        for (PointMarkerView pointMarkerView : carPointMarkerViewList) {
            pointMarkerView.setVisible(false);
        }

    }

    public void removeFromMap() {

        for (PointMarkerView pointMarkerView : carPointMarkerViewList) {
            pointMarkerView.removeFromMap();
        }

    }


    public void destory() {

        for (PointMarkerView pointMarkerView : carPointMarkerViewList) {
            pointMarkerView.destory();
        }

    }


    private PointMarkerView produce() {
        PointMarkerView view = new PointMarkerView.Builder(getContext(), getAmap())
                .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.car))
                .setZindex(3)
                .setAnchor(0.5f, 0.5f)
                .create();
        return view;

    }

    public AMap getAmap() {
        return amap;
    }

    public Context getContext() {
        return context;
    }
}
