package com.yisingle.app.data;

import com.amap.api.maps.offlinemap.OfflineMapCity;

/**
 * Created by jikun on 17/5/25.
 */

public class CityData {
    private String adcode;
    private String city;
    private String code;
    private String jianpin;
    private String pinyin;

    public CityData() {

    }

    public CityData(OfflineMapCity mapCity) {
        if (mapCity == null) {
            return;
        }
        this.adcode = mapCity.getAdcode();
        this.city = mapCity.getCity();
        this.code = mapCity.getCode();
        this.jianpin = mapCity.getJianpin();
        this.pinyin = mapCity.getPinyin();
        this.type = 0;
    }

    public static CityData createGroupModel(char groupChar) {
        CityData res = new CityData();
        res.city = String.valueOf(groupChar);
        res.type = 1;
        return res;
    }

    public boolean isGroupModel() {
        return type == 1;
    }

    /**
     * 在选择城市列表展示时会用到。0 == 普通的城市数据 ， 1==group数据。
     */
    public int type;

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getJianpin() {
        return jianpin;
    }

    public void setJianpin(String jianpin) {
        this.jianpin = jianpin;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
}
