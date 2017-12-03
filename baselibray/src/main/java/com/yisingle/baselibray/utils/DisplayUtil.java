package com.yisingle.baselibray.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by yangshuai in the 15:40 of 2015.11.26 .
 * dp, sp, 与 px 互相转换的工具类
 */
@SuppressWarnings("unused")
public class DisplayUtil {

    /**
     * 将 px 转换为 dip 或 dp， 保证尺寸大小不变
     *
     * @param context  上下文
     * @param pxValue  像素 px
     * @return  返回dp值
     */
    public static int px2dip(Context context, float pxValue) {

        /* density 是屏幕比例因子， 以 160dpi（1px = 1dp） 为标准 density 值为1，320dpi（2px = 1dp） 中 density 值为 2（320/160） */
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 將 dip 或 dp 转换为 px, 保证尺寸大小不变
     *
     * @param context  上下文
     * @param dipValue  dp值
     * @return   像素px
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将 px 转换为 sp， 保证尺寸大小不变
     *
     * @param context  上下文
     * @param pxValue  像素px值
     * @return  返回文本的sp值
     */
    public static int px2sp(Context context, float pxValue) {

        /* scaledDensity，字体的比例因子，类似 density， 会根据用户偏好返回不同的值*/
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将 sp 转换为 px， 保证尺寸大小不变
     *
     * @param context  上下文
     * @param pxValue  sp值
     * @return   返回像素px值
     */
    public static int sp2px(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue * fontScale + 0.5f);
    }

    /**
     * 使用系统工具类 TypedValue 帮助把 数值 转换到 px
     *
     * @param context  上下文
     * @param dp     dp值
     * @return   返回像素px值
     */
    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}