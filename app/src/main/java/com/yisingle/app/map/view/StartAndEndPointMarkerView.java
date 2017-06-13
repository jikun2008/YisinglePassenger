package com.yisingle.app.map.view;

import android.content.Context;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.yisingle.app.data.MapPointData;
import com.yisingle.app.utils.BitMapUtils;
import com.yisingle.app.utils.DisplayUtil;
import com.yisingle.app.utils.GLFont;
import com.yisingle.app.utils.ScreenUtils;

/**
 * Created by jikun on 17/6/13.
 */

public class StartAndEndPointMarkerView extends BaseMapMarkerView {

    private PointMapMarkerView startPointMapMarkerView;

    private PointMapMarkerView endPointMapMarkerView;


    private LatLng startLatLng;

    private LatLng endtLatLng;

    private int paddingheight;
    private int paddingwidth;

    private int size;


    public StartAndEndPointMarkerView(Context mContext, int size) {
        super(mContext);
        this.size = size;
        startPointMapMarkerView = new PointMapMarkerView(mContext, size);
        endPointMapMarkerView = new PointMapMarkerView(mContext, size);
    }


    public void addMarkViewToMap(MapPointData startData, MapPointData endData, AMap aMap) {
        startPointMapMarkerView.addMarkViewToMap(startData.getText(), startData.getLatLng(), startData.getRes(), aMap);
        endPointMapMarkerView.addMarkViewToMap(endData.getText(), endData.getLatLng(), endData.getRes(), aMap);
        startLatLng = startData.getLatLng();
        endtLatLng = endData.getLatLng();


        int hg[] = BitMapUtils.getImageWidthHeight(mContext.getResources(), startData.getRes());
        paddingwidth = hg[0];
        paddingheight = hg[1];

    }

    public void moveToCamera(AMap aMap) {
        LatLngBounds latLngBounds = new LatLngBounds.Builder().include(startLatLng).include(endtLatLng).build();

        float startright = GLFont.getFontlength(startPointMapMarkerView.getText(), size);
        float endright = GLFont.getFontlength(endPointMapMarkerView.getText(), size);
        int screenWidth = ScreenUtils.getScreenWidth();

        int left = paddingwidth + (int) (startright > endright ? startright : endright);
        if (left > screenWidth /2) {
            left = screenWidth / 5;
        }
        int top = paddingheight * 2;
        int right = left;
        int bottom = DisplayUtil.dip2px(mContext, 210) + top;


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("----paddingwidth=" + paddingwidth);
        stringBuilder.append("----paddingheight=" + paddingheight);
        stringBuilder.append("----startright=" + startright);
        stringBuilder.append("----endright=" + endright);
        stringBuilder.append("----top=" + top);
        stringBuilder.append("----bottom=" + bottom);
        stringBuilder.append("----left=" + left);
        stringBuilder.append("----right=" + right);
        Log.e("测试代码", "测试代码moveToCamera---" + stringBuilder.toString());
        aMap.animateCamera(CameraUpdateFactory.newLatLngBoundsRect(latLngBounds, left, right, top, bottom));

    }

    @Override
    public void removeMarkerViewFromMap() {
        super.removeMarkerViewFromMap();
        startPointMapMarkerView.removeMarkerViewFromMap();
        endPointMapMarkerView.removeMarkerViewFromMap();
    }

    public LatLng getStartLatLng() {
        return startLatLng;
    }

    public void setStartLatLng(LatLng startLatLng) {
        this.startLatLng = startLatLng;
    }

    public LatLng getEndtLatLng() {
        return endtLatLng;
    }

    public void setEndtLatLng(LatLng endtLatLng) {
        this.endtLatLng = endtLatLng;
    }
}
