package com.yisingle.app.data;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jikun on 17/6/6.
 */

public class FastCarPriceData {
    private String typeName;
    private String price;
    private String discountPrice;

    private boolean isselector;

    @PriceType
    int type;

    //添加支持注解的依赖到你的项目中，需要在build.gradle文件中的依赖块中添加：
    //dependencies { compile 'com.android.support:support-annotations:24.2.0' }
    @IntDef({PriceType.Excellent, PriceType.Carpooling, PriceType.NOCarpooling})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PriceType {


        int Carpooling = 1;
        int NOCarpooling = 2;
        int Excellent = 3;


    }

    public int getType() {
        return type;
    }


    public boolean isselector() {
        return isselector;
    }

    public void setIsselector(boolean isselector) {
        this.isselector = isselector;
    }

    public void setType(@PriceType int type) {
        this.type = type;
    }

    public static FastCarPriceData createCarpoolingPriceData(boolean isselector, String typeName, String price, String discountPrice) {
        FastCarPriceData fastCarPriceData = new FastCarPriceData();
        fastCarPriceData.setIsselector(isselector);
        fastCarPriceData.setType(PriceType.Carpooling);
        fastCarPriceData.setTypeName(typeName);
        fastCarPriceData.setPrice(price);
        fastCarPriceData.setDiscountPrice(discountPrice);
        return fastCarPriceData;

    }

    public static FastCarPriceData createNOCarpoolingPriceData(boolean isselector, String typeName, String price, String discountPrice) {
        FastCarPriceData fastCarPriceData = new FastCarPriceData();
        fastCarPriceData.setIsselector(isselector);
        fastCarPriceData.setType(PriceType.NOCarpooling);
        fastCarPriceData.setTypeName(typeName);
        fastCarPriceData.setPrice(price);
        fastCarPriceData.setDiscountPrice(discountPrice);
        return fastCarPriceData;
    }

    public static FastCarPriceData createExcellentPriceData(boolean isselector, String typeName, String price, String discountPrice) {
        FastCarPriceData fastCarPriceData = new FastCarPriceData();
        fastCarPriceData.setIsselector(isselector);
        fastCarPriceData.setType(PriceType.Excellent);
        fastCarPriceData.setTypeName(typeName);
        fastCarPriceData.setPrice(price);
        fastCarPriceData.setDiscountPrice(discountPrice);
        return fastCarPriceData;
    }


    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }
}
