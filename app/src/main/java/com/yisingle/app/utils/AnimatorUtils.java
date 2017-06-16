package com.yisingle.app.utils;

import android.animation.ValueAnimator;

import android.view.animation.LinearInterpolator;


/**
 * Created by jikun on 17/6/16.
 */

public class AnimatorUtils {

    public static ValueAnimator getValueAnimator(int min, int max, ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(min, max);
        valueAnimator.setDuration(3000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(animatorUpdateListener);
        //无限循环
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        //从头开始动画
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.start();


        return valueAnimator;
    }


}
