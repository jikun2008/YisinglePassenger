package com.yisingle.app.data;

import android.support.annotation.DrawableRes;

import com.yisingle.app.R;

/**
 * Created by jikun on 17/6/6.
 */

public class FastCarTypeData {
    private boolean isselector = false;
    private String typeName = "";
    private
    @DrawableRes
    int typeIcon;


    public static FastCarTypeData createNormalTypeData(boolean isselector, String typeName) {
        FastCarTypeData fastCarTypeData = new FastCarTypeData();
        fastCarTypeData.setIsselector(isselector);
        fastCarTypeData.setTypeName(typeName);
        fastCarTypeData.setTypeIcon(R.drawable.fast_car_average_type);
        return fastCarTypeData;

    }

    public static FastCarTypeData createExcellentTypeData(boolean isselector, String typeName) {
        FastCarTypeData fastCarTypeData = new FastCarTypeData();
        fastCarTypeData.setIsselector(isselector);
        fastCarTypeData.setTypeName(typeName);
        fastCarTypeData.setTypeIcon(R.drawable.fast_car_excellent_type);
        return fastCarTypeData;
    }

    public boolean isselector() {
        return isselector;
    }

    public void setIsselector(boolean isselector) {
        this.isselector = isselector;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeIcon() {
        return typeIcon;
    }

    public void setTypeIcon(@DrawableRes int typeIcon) {
        this.typeIcon = typeIcon;
    }
}
