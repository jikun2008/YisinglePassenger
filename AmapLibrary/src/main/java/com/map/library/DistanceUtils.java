package com.map.library;

import java.math.BigDecimal;

/**
 * Created by jikun on 17/7/4.
 */

public class DistanceUtils {

    public static String getDistance(int rice) {
        String info = "";
        if (rice > 1000) {
            double ditance = rice * 0.001;
            BigDecimal b = new BigDecimal(ditance);
            double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            info = f1 + "公里";
        } else {
            info = rice + "米";
        }


        return info;
    }
}
