package com.yisingle.app.map.view;

import android.content.Context;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLngBounds;
import com.yisingle.app.map.view.PointMapMarkerView.PointMapMarkerData;
import com.yisingle.app.map.view.PointMapMarkerView.PointMapWindowData;
import com.yisingle.app.utils.BitMapUtils;
import com.yisingle.app.utils.DisplayUtil;
import com.yisingle.app.utils.GLFont;
import com.yisingle.app.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jikun on 17/6/13.
 */

public class StartAndEndPointListMarkerView extends BaseMapListMarkerView<PointMapMarkerView.PointMapMarkerData, PointMapMarkerView.PointMapWindowData> {


    public StartAndEndPointListMarkerView(AMap map, Context context) {
        super(map, context);

    }

    @Override
    protected List<BaseMapMarkerView<PointMapMarkerData, PointMapWindowData>> getListMarker(List<PointMapMarkerData> markerDataList) {

        List<BaseMapMarkerView<PointMapMarkerData, PointMapWindowData>> listView = new ArrayList<>();

        for (PointMapMarkerData data : markerDataList) {
            PointMapMarkerView pointMapMarkerView = new PointMapMarkerView(getMap(), getContext());
            pointMapMarkerView.addView(data);
            listView.add(pointMapMarkerView);
        }

        return listView;
    }


    public void moveToCamera() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();


        int hg[] = {0, 0};
        float startright = 0;
        float endright = 0;
        List<PointMapMarkerData> mapMarkerDataList = getMarkerDataList();
        for (int i = 0; i < mapMarkerDataList.size(); i++) {
            PointMapMarkerData pointMapMarkerData = mapMarkerDataList.get(i);
            builder.include(pointMapMarkerData.getLatLng());

            if (i == 0) {
                hg = BitMapUtils.getImageWidthHeight(getContext().getResources(), pointMapMarkerData.getDrawableId());
                startright = GLFont.getFontlength(pointMapMarkerData.getText(), pointMapMarkerData.getSize());
            } else if (i == 1) {
                endright = GLFont.getFontlength(pointMapMarkerData.getText(), pointMapMarkerData.getSize());
            }

        }

        LatLngBounds latLngBounds = builder.build();

        int paddingwidth = hg[0];
        int paddingheight = hg[1];

        int screenWidth = ScreenUtils.getScreenWidth();

        int left = paddingwidth + (int) (startright > endright ? startright : endright);
        if (left > screenWidth / 2) {
            left = screenWidth / 5;
        }
        int top = paddingheight * 2;
        int right = left;
        int bottom = DisplayUtil.dip2px(getContext(), 210) + top;


        getMap().animateCamera(CameraUpdateFactory.newLatLngBoundsRect(latLngBounds, left, right, top, bottom));

    }


}
