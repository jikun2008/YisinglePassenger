package com.map.library.view.utils;

import android.text.TextUtils;

import com.amap.api.services.geocoder.RegeocodeAddress;

/**
 * Created by jikun on 17/5/16.
 * 获取简单逆地理返回的名称
 */

public class RegeocodeAddressInfoUtils {


    public static String getRegeocodeAddress(RegeocodeAddress address) {
        String info = new StringBuilder()
                .append("所在区（县）的编码:").append(address.getAdCode()).append("\n")
                .append("的建筑物名称:").append(address.getBuilding()).append("\n")
                .append("城市名称:").append(address.getCity()).append("\n")
                .append("所在区（县）名称:").append(address.getDistrict()).append("\n")
                .append("社区名称:").append(address.getNeighborhood()).append("\n")
                .append("省名称、直辖市的名称:").append(address.getProvince()).append("\n")
                .append("门牌信息:").append(address.getStreetNumber()).append("\n")
                .append("乡镇名称:").append(address.getTownship()).append("\n")
                .append("全名:").append(address.getFormatAddress()).append("\n").toString();
        return info;

    }

    /**
     * 获取简单位置名称，详细名称会去掉省市县乡的信息
     * 例如   全名：四川省成都市武侯区桂溪街道天府一街   返回的是天府一街
     *
     * @param regeocodeAddress regeocodeAddress
     * @return String
     */
    public static String getSimpleSitename(RegeocodeAddress regeocodeAddress) {
        String name = new StringBuilder()
                //省名称、直辖市的名称
                .append(regeocodeAddress.getProvince())
                //城市名称
                .append(regeocodeAddress.getCity())
                //所在区（县）名称:
                .append(regeocodeAddress.getDistrict())
                //乡镇名称:
                .append(regeocodeAddress.getTownship())
                .append(regeocodeAddress.getNeighborhood())
                .toString();


        String address = regeocodeAddress.getFormatAddress().replace(name, "");
        if (TextUtils.isEmpty(address)) {
            address = regeocodeAddress.getNeighborhood();
        }
        if (TextUtils.isEmpty(address)) {
            address = regeocodeAddress.getFormatAddress();
        }
        return address;
    }
}
