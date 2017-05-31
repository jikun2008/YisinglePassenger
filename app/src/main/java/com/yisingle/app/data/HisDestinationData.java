package com.yisingle.app.data;

import android.support.annotation.DrawableRes;

import com.yisingle.app.R;

/**
 * Created by jikun on 17/5/31.
 */

public class HisDestinationData {
    private String name;
    private String allName;
    private

    int icon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAllName() {
        return allName;
    }

    public void setAllName(String allName) {
        this.allName = allName;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(@DrawableRes int icon) {
        this.icon = icon;
    }

    public HisDestinationData(String name, String allName, @DrawableRes int icon) {
        this.name = name;
        this.allName = allName;
        this.icon = icon;
    }

    public static HisDestinationData createNormalHisDestinationData(String name, String allName) {
        HisDestinationData hisDestinationData = new HisDestinationData(name, allName, R.mipmap.icon_destination_history);
        return hisDestinationData;
    }

    public static HisDestinationData createHomeHisDestinationData(String name, String allName) {
        HisDestinationData hisDestinationData = new HisDestinationData(name, allName, R.mipmap.icon_destination_home);
        return hisDestinationData;
    }

    public static HisDestinationData createCompanyHisDestinationData(String name, String allName) {
        HisDestinationData hisDestinationData = new HisDestinationData(name, allName, R.mipmap.icon_destination_company);
        return hisDestinationData;
    }


}
