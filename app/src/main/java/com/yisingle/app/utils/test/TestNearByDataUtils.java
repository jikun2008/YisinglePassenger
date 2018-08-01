package com.yisingle.app.utils.test;

import android.support.annotation.NonNull;

import com.amap.api.maps.model.LatLng;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jikun
 * Created by jikun on 2018/6/21.
 */
public class TestNearByDataUtils {


    public static List<List<LatLng>> produceList(int random, LatLng center) {

        List<List<LatLng>> list = new ArrayList<>();
        int size = 1 + (int) (Math.random() * random);

        for (int i = 0; i < size; i++) {
            list.add(produceLatLngList(random, center));
        }
        return list;


    }


    public static List<LatLng> produceLatLngList(int random, @NonNull LatLng center) {
        List<LatLng> list = new ArrayList<>();
        int size = 1 + (int) (Math.random() * random);

       // Log.e("测试代码", "测试代码size=" + size);

        for (int i = 0; i < size; i++) {
            list.add(randomArroundLatLng(center));
        }


        return list;


    }


    private static LatLng randomArroundLatLng(LatLng center) {


        double random = Math.random() - 0.5;
        double random1 = Math.random() - 0.5;

        double nowlatitude = formatDouble2(center.latitude + random * 0.01, 6);
        double nowlongitude = formatDouble2((center.longitude + random1 * 0.01), 6);

        return new LatLng(nowlatitude, nowlongitude);


    }


    private static double formatDouble2(double d, int point) {
        // 旧方法，已经不再推荐使用
        //BigDecimal bg = new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP);

        // 新方法，如果不需要四舍五入，可以使用RoundingMode.DOWN
        BigDecimal bg = new BigDecimal(d).setScale(point, RoundingMode.UP);


        return bg.doubleValue();
    }


}