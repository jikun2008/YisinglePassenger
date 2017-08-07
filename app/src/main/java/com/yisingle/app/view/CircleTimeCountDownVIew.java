package com.yisingle.app.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jikun on 17/8/4.
 * 防滴滴圆形倒计时的功能。
 * 小圆点坐标Y  Y=CenterY+R*Sin(角度)
 * 小圆点坐标X  X=CenterX+R*Cos(角度)
 * 1.先画个圆形当背景 fef7f3
 * 2.然后画个圆弧
 * 3.画小圆点 颜色值#ff984c
 * 4.添加文字
 */

public class CircleTimeCountDownVIew extends RelativeLayout {

    private final String TAG = CircleTimeCountDownVIew.class.getSimpleName();

    private float width = 0;

    private float height = 0;

    private Paint mBackGroudPaint;

    private Paint mArcPaint;

    private Paint mPointPaint;

    private TextView textView;

    private float centerX;

    private float centerY;


    //Color.parseColor("#fef7f3");
    private int lightOrangeColor = Color.parseColor("#fef7f3");

    private int darkOrangeColor = Color.parseColor("#ff984c");


    private float strokeWidth = 2;//圆弧宽度

    private float littePointRadius = 8;//小圆点半径

    private int padding;


    double ratio = 0;

    ValueAnimator animator = null;


    private long allTime = 5 * 60 * 1000;


    public CircleTimeCountDownVIew(Context context) {
        super(context);

    }

    public CircleTimeCountDownVIew(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        mBackGroudPaint = new Paint();
        mBackGroudPaint.setAntiAlias(true);//开启锯齿
        mBackGroudPaint.setStyle(Paint.Style.FILL);
        mBackGroudPaint.setColor(lightOrangeColor);//背景圆圈设置颜色

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Paint.Style.STROKE);//STROKE画圆弧  FILL画扇形
        mArcPaint.setStrokeWidth(strokeWidth);
        mArcPaint.setColor(darkOrangeColor);

        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);//STROKE画圆弧  FILL画扇形
        mPointPaint.setColor(darkOrangeColor);

        padding = (int) (strokeWidth + littePointRadius / 2 + 1);

        initTextView(context);
        // startWithtoOrgitAnimation(0f, 1f);

    }

    private void initTextView(Context context) {

        textView = new TextView(context);
        textView.setTextColor(Color.parseColor("#ff984c"));
        textView.setText("05'00\"");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        textView.setGravity(Gravity.CENTER);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(CENTER_IN_PARENT);
        addView(textView, layoutParams);
    }


    static String regEx = "[\u4e00-\u9fa5]";
    static Pattern pat = Pattern.compile(regEx);

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//
//        boolean isHaveChinese = isContainsChinese(textView.getText().toString());
//        int lenth = textView.getText().length();
//        float textsize;
//        if (lenth == 0) {
//            lenth = 1;
//        }
//        if (isHaveChinese) {
//
//            if (lenth > 1) {
//                lenth = lenth * 2 - 2;
//
//            }
//
//
//        } else {
//
//            if (lenth > 2) {
//                lenth = lenth - 2;
//            }
//
//        }
//        textsize = getWidth() / lenth;
//
//        Log.e(TAG, TAG + "onSizeChanged---textsize=" + textsize + "length=" + lenth);
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize);
//
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();      //保存状态
        //具体操作

        /**
         * 这是一个居中的圆
         */
        width = getWidth();
        height = getHeight();
        Log.e(TAG, TAG + "width=" + width + "");
        Log.e(TAG, TAG + "height=" + height + "");

        float radius = 0;
        if (width > height) {
            radius = height / 2;
        } else {
            radius = width / 2;
        }

        centerX = radius;
        centerY = centerX;
        radius = radius - padding;

        Log.e(TAG, TAG + "centerX=" + centerX + "");
        Log.e(TAG, TAG + "centerY=" + centerY + "");
        Log.e(TAG, TAG + "radius=" + radius + "");
        drawCircle(canvas, centerX, centerY, radius, mBackGroudPaint);//画背景圆

        drawArc(canvas, centerX, centerY, radius, ratio, mArcPaint);//画圆弧

        float startAngle = (float) (360 * ratio - 90);//起点的角度
        float littleCenterX = (float) (centerX + radius * Math.cos(Math.PI * startAngle / 180));
        float littleCenterY = (float) (centerY + radius * Math.sin(Math.PI * startAngle / 180));
        Log.e(TAG, TAG + "littleCenterX=" + littleCenterX + "littleCenterY=" + littleCenterY);
        Log.e(TAG, TAG + "startAngle=" + startAngle);
        Log.e(TAG, TAG + "Math.sin(startAngle)=" + Math.sin(Math.PI * startAngle / 180));
        drawCircle(canvas, littleCenterX, littleCenterY, littePointRadius, mPointPaint);//画小圆点
        canvas.restore();   //回滚到之前的状态
    }


    private void drawCircle(Canvas canvas, float centerX, float centerY, float radius, Paint paint) {
        canvas.drawCircle(centerX, centerY, radius, paint);
    }

    private void drawArc(Canvas canvas, float centerX, float centerY, float radius, double ratio, Paint paint) {
        float left = centerX - radius;
        float top = centerY - radius;
        float right = centerX + radius;
        float bottom = centerY + radius;
        RectF oval = new RectF(left, top,
                right, bottom);

        float startAngle = (float) (360 * ratio - 90);//起点的角度
        float sweepAngle = 270 - startAngle;//圆圈旋转的角度

        canvas.drawArc(oval, startAngle, sweepAngle, false, paint);
    }


    public static boolean isContainsChinese(String str) {
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }

    public void startCoutDown() {
        startWithtoOrgitAnimation(0, 1f);

    }

//    public void startToAnimal(float nowCurrent) {
//        float ratio1 = (float) ratio;
//        startWithtoOrgitAnimation(ratio1, nowCurrent);
//    }


    private void startWithtoOrgitAnimation(float currentheight, float toheight) {

        //为了看到动画值的变化，这里添加了动画更新监听事件来打印动画的当前值


        Log.e(TAG, TAG + "currentheight=" + currentheight + "---toheight=" + toheight);
        animator = ValueAnimator.ofFloat(currentheight, toheight);


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                //Log.e(TAG, TAG + "测试代码value=" + value + "");


                float value = (Float) animation.getAnimatedValue();
//                Log.e(TAG, TAG + "测试代码value=" + value + "");
                ratio = value;


                BigDecimal b = new BigDecimal(ratio);
                double f1 = b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
                long time = (long) (allTime * f1);

                Log.e(TAG, TAG + "测试代码value=" + value + "--time=" + time + "--f1=" + f1);
                setTimeTextView(time);
                invalidate();


            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.e(TAG, TAG + "测试代码Animator=" + "--onAnimationStart=");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e(TAG, TAG + "测试代码Animator=" + "--onAnimationEnd=");

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.e(TAG, TAG + "测试代码Animator=" + "--onAnimationCancel=");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.e(TAG, TAG + "测试代码Animator=" + "--onAnimationRepeat=");
            }
        });
        animator.setDuration(allTime);//动画时间
        animator.start();//启动动画
    }

    public void stopCountTime() {
        if (null != animator) {
            animator.pause();
            animator.end();
            animator = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e(TAG, TAG + "----onDetachedFromWindow");
        if (null != animator) {
            animator.end();
            animator = null;
        }
    }


    private void setTimeTextView(long currentTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String time = TimeUtils.millis2String(allTime - currentTime, sdf);
        textView.setText(time);
    }

}
