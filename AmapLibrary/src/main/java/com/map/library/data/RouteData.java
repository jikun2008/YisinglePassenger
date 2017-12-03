package com.map.library.data;

import android.support.annotation.IntDef;

import com.amap.api.services.route.DriveRouteResult;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jikun on 17/6/22.
 */

public class RouteData {

    private DriveRouteResult driveRouteResult;


    @Type
    private int type = 0;

    //添加支持注解的依赖到你的项目中，需要在build.gradle文件中的依赖块中添加：
    //dependencies { compile 'com.android.support:support-annotations:24.2.0' }
    @IntDef({Type.CAR_TO_TARGET, Type.START_TO_END})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {

        int CAR_TO_TARGET = 0;
        int START_TO_END = 1;

    }

    private RouteData() {

    }

    public static RouteData  createRouteData(DriveRouteResult driveRouteResult, @Type int type) {
        RouteData routeData = new RouteData();
        routeData.setDriveRouteResult(driveRouteResult);
        routeData.setType(type);
        return routeData;
    }

    public DriveRouteResult getDriveRouteResult() {
        return driveRouteResult;
    }

    public void setDriveRouteResult(DriveRouteResult driveRouteResult) {
        this.driveRouteResult = driveRouteResult;
    }

    public
    @Type
    int getType() {
        return type;
    }

    public void setType(@Type int type) {
        this.type = type;
    }
}
