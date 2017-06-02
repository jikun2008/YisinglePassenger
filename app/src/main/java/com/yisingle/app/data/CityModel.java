package com.yisingle.app.data;

import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.yisingle.app.decoration.SuspensionDecoration;
import com.yisingle.app.widget.IndexBar.bean.BaseIndexPinyinBean;

/**
 * Created by jikun on 17/5/25.
 */

public class CityModel extends BaseIndexPinyinBean implements SuspensionDecoration.ISuspensionInterface {
    private String adcode;
    private String city;
    private String code;
    private String jianpin;
    private String pinyin;

    private boolean isHotCity = false;

    public boolean isHotCity() {
        return isHotCity;
    }

    public void setHotCity(boolean hotCity) {
        isHotCity = hotCity;
    }

    public CityModel() {

    }


    /**
     * 建立新热点城市
     *
     * @return
     */
    public static CityModel createHotCityModel(CityModel oldcityModel) {
        CityModel cityModel = new CityModel();
        cityModel.setAdcode(oldcityModel.getAdcode());
        cityModel.setCity(oldcityModel.getCity());
        cityModel.setCode(oldcityModel.getCode());
        cityModel.setJianpin(oldcityModel.getJianpin());
        cityModel.setPinyin(oldcityModel.pinyin);
        cityModel.setHotCity(true);
        return cityModel;

    }

    public CityModel(OfflineMapCity mapCity) {
        if (mapCity == null) {
            return;
        }
        this.adcode = mapCity.getAdcode();
        this.city = mapCity.getCity();
        this.code = mapCity.getCode();
        this.jianpin = mapCity.getJianpin();
        this.pinyin = mapCity.getPinyin();
    }


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

    @Override
    public boolean isShowSuspension() {
        return true;
    }

    @Override
    public String getSuspensionTag() {

        String sustag;
        if (isHotCity) {
            sustag = "热";

        } else {
            sustag = String.valueOf(pinyin.charAt(0));

        }
        return sustag;

    }

    @Override
    public String getTarget() {
        return city;
    }


    @Override
    public String getBaseIndexPinyin() {
        if (isHotCity) {
            return "热";
        } else {
            return pinyin;
        }

    }

    @Override
    public String toString() {
        return "CityModel{" +
                "adcode='" + adcode + '\'' +
                ", city='" + city + '\'' +
                ", code='" + code + '\'' +
                ", jianpin='" + jianpin + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", isHotCity=" + isHotCity +
                '}';
    }
}
