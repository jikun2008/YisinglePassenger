package com.yisingle.app.data;

import android.support.annotation.IntDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jikun on 17/7/21.
 */
public class SocketHeadData implements Serializable {


    private int type;

    private int code;

    private String msg;


    public
    @Type
    int getType() {
        return type;
    }

    public void setType(@Type int type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    //添加支持注解的依赖到你的项目中，需要在build.gradle文件中的依赖块中添加：
    //dependencies { compile 'com.android.support:support-annotations:24.2.0' }
    @IntDef({Type.HEART_BEAT, Type.ORDER_NEW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {

        int HEART_BEAT = 1;
        int ORDER_NEW = 2;
        int PRICIE_ORDER=3;

    }


}
