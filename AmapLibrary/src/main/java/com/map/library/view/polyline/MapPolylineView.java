package com.map.library.view.polyline;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.TMC;
import com.amap.library.R;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by jikun on 17/6/21.
 */

public class MapPolylineView {


    private AMap mAMap;

    private Context mContext;

    private float mWidth = 40f;


    private List<LatLng> totalLatLng = new ArrayList<>();//线路中所有的坐标

    private List<TMC> traffictmcs = new ArrayList<>();//线路中交通情况

    private BitmapDescriptor arrowOnRoute = null;
    private BitmapDescriptor unknownTraffic = null;
    private BitmapDescriptor smoothTraffic = null;
    private BitmapDescriptor slowTraffic = null;
    private BitmapDescriptor jamTraffic = null;
    private BitmapDescriptor veryJamTraffic = null;


    private Polyline arrowpolyline;

    private List<Polyline> trafficPolylineList = new ArrayList<>();//显示交通状态的Polyline列表


    public MapPolylineView(Context context, AMap mAMap) {
        initTrafficStausBitMap();
        mContext = context;
        this.mAMap = mAMap;
        this.mWidth = (float) pxToDp(context, 22);

    }

    /**
     * 初始化交通状态的图片
     */
    private void initTrafficStausBitMap() {

        this.arrowOnRoute = BitmapDescriptorFactory.fromResource(R.mipmap.custtexture_aolr);
        this.smoothTraffic = BitmapDescriptorFactory.fromResource(R.mipmap.custtexture_green);
        this.unknownTraffic = BitmapDescriptorFactory.fromResource(R.mipmap.custtexture_no);
        this.slowTraffic = BitmapDescriptorFactory.fromResource(R.mipmap.custtexture_slow);
        this.jamTraffic = BitmapDescriptorFactory.fromResource(R.mipmap.custtexture_bad);
        this.veryJamTraffic = BitmapDescriptorFactory.fromResource(R.mipmap.custtexture_grayred);

    }

    public void addLine(DrivePath drivePath) {
        if (null != totalLatLng) {
            totalLatLng.clear();
        }

        if (null != traffictmcs) {
            traffictmcs.clear();
        }

        List<DriveStep> driveStepList = drivePath.getSteps();
        for (DriveStep step : driveStepList) {
            //根据数据来区分将
            List<LatLonPoint> latlonPoints = step.getPolyline();
            traffictmcs.addAll(step.getTMCs());//获取交通状态不同的坐标点的集合
            for (LatLonPoint latlonpoint : latlonPoints) {
                totalLatLng.add(convertToLatLng(latlonpoint));//将所有的点添加到totalLatLng中为后面使用做准备
            }
        }

        addTrafficStateLine(traffictmcs);
        addArrowLine();

    }


    //添加箭头图片地图线
    private void addArrowLine() {

        if (null != mAMap) {
            PolylineOptions options = new PolylineOptions().addAll(totalLatLng).width(this.mWidth).setCustomTexture(this.arrowOnRoute);

            arrowpolyline = mAMap.addPolyline(options);
        }

    }


    /**
     * 根据不同的路段拥堵情况展示不同的颜色
     *
     * @param tmcList
     */
    private void addTrafficStateLine(List<TMC> tmcList) {
        if (mAMap == null) {
            return;
        }
        if (tmcList == null || tmcList.size() <= 0) {
            return;
        }


        TMC previousTmc = null;//这是下面循环中上一次的交通坐标集合类，用来防止出现多条线之间的断点问题。
        for (int i = 0; i < tmcList.size(); i++) {
            LatLng previousLatLng = null;//得到previousTmc的最后的点
            if (null != previousTmc && null != previousTmc.getPolyline() && previousTmc.getPolyline().size() > 0) {
                int num = previousTmc.getPolyline().size() - 1;
                LatLonPoint lastLatLonPoint = previousTmc.getPolyline().get(num);
                previousLatLng = new LatLng(lastLatLonPoint.getLatitude(), lastLatLonPoint.getLongitude());
            }


            PolylineOptions options = getPolylineOptionsByTrafficStaus(tmcList.get(i), previousLatLng);


            Polyline polyline = mAMap.addPolyline(options);

            trafficPolylineList.add(polyline);

            previousTmc = tmcList.get(i);//循环结束赋值给previousTmc，保留这个对象。
        }
    }


    public void removeLine() {
        if (null != arrowpolyline) {
            arrowpolyline.remove();
        }

        if (null != trafficPolylineList) {
            for (Polyline polyline : trafficPolylineList) {
                if (null != polyline) {
                    polyline.remove();
                    ;
                }
            }
        }


    }

    /**
     * 2017年04月01日15:46:40
     * 根据交通状态返回PolylineOptions图
     *
     * @param tmc
     * @return
     */
    private PolylineOptions getPolylineOptionsByTrafficStaus(TMC tmc, LatLng previousLatLng) {
        PolylineOptions polylineOptions = new PolylineOptions();
        String status = tmc.getStatus();
        if (status == null) {
            status = "";
        }

        List<LatLonPoint> mployline = tmc.getPolyline();
        List<LatLng> latLngList = new ArrayList<>();
        if (previousLatLng != null) {
            //如果传递过来是上一个地图线的最后坐标不为null
            // 那么把这个坐标点添加到地图线的坐标前面去防止多条线段的断点问题。
            latLngList.add(previousLatLng);
        }

        for (LatLonPoint latLonPoint : mployline) {
            latLngList.add(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()));
        }


        if (status.equals("畅通")) {
            polylineOptions.setCustomTexture(this.smoothTraffic);

        } else if (status.equals("缓行")) {
            polylineOptions.setCustomTexture(this.slowTraffic);

        } else if (status.equals("拥堵")) {
            polylineOptions.setCustomTexture(this.jamTraffic);

        } else if (status.equals("严重拥堵")) {
            polylineOptions.setCustomTexture(this.veryJamTraffic);

        } else {
            polylineOptions.setCustomTexture(this.unknownTraffic);

        }

        polylineOptions.addAll(latLngList).width(this.mWidth).color(Color.argb(255, 1, 1, 1));
        return polylineOptions;

    }

    private LatLng convertToLatLng(LatLonPoint point) {
        return new LatLng(point.getLatitude(), point.getLongitude());
    }

    private int pxToDp(Context context, int px) {
        try {
            if (px == 0) {
                return 0;
            } else if (context == null) {
                return px;
            } else {
                float var2 = TypedValue.applyDimension(1, (float) px, context.getResources().getDisplayMetrics());
                return (int) var2;
            }
        } catch (Exception var3) {
            var3.printStackTrace();
            return px;
        }
    }


}
