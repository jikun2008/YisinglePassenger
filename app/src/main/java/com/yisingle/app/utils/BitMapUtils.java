package com.yisingle.app.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.DrawableRes;
import android.util.Log;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;

/**
 * Created by jikun on 17/5/12.
 */

public class BitMapUtils {

    /**
     * 合并两张bitmap为一张
     *
     * @param background 第一张图片
     * @param foreground 第二张图片
     * @return Bitmap
     */
    @SuppressWarnings("unused")
    public static Bitmap combineBitmap(Bitmap background, Bitmap foreground) {
        if (background == null) {
            return null;
        }
        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        int fgWidth = foreground.getWidth();
        int fgHeight = foreground.getHeight();
        Bitmap newmap = Bitmap
                .createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newmap);
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(foreground, (bgWidth - fgWidth) / 2,
                (bgHeight - fgHeight) / 2, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newmap;
    }


    public static Bitmap combineTextBitmap() {

        Bitmap newmap = Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newmap);
        String testString = "测试：gafaeh:1234";
        Paint mPaint = new Paint();
        mPaint.setStrokeWidth(3);
        mPaint.setTextSize(40);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextAlign(Paint.Align.LEFT);
        Rect bounds = new Rect();
        mPaint.getTextBounds(testString, 0, testString.length(), bounds);
        canvas.drawText(testString, 0, 0, mPaint);

        canvas.save(Canvas.ALL_SAVE_FLAG);

        canvas.restore();
        return newmap;
    }

    /**
     * 合并两张bitmap为一张
     *
     * @param background 定位图片
     * @return Bitmap
     */

    public static Bitmap combineCenterBitmap(Bitmap background, float percent) {
        if (background == null) {
            return null;
        }

        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();

        int min = 5;
        int max = bgWidth / 2;
        int difference = max - min;
        float radius = min + difference * percent + 1;

        Bitmap newmap = Bitmap
                .createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        canvas.drawBitmap(background, 0, 0, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        canvas.drawCircle(bgWidth / 2, bgWidth / 2 + 1, radius, paint);
        canvas.save(Canvas.ALL_SAVE_FLAG);

        canvas.restore();
        return newmap;
    }


    public static BitmapDescriptor combineCenterBitmapDescriptor(Bitmap background, float percent) {
        return BitmapDescriptorFactory.fromBitmap(combineCenterBitmap(background, percent));
    }

    /**
     * 获取多个BitmapDescriptor的列表
     * 用来设置给Marker进行gif动画
     *
     * @return 返回中心Marker的动画效果图片组
     */
    public static ArrayList<BitmapDescriptor> getMultipleBitmapDescriptorList(Bitmap bitmap) {
        ArrayList<BitmapDescriptor> list = new ArrayList<>();
        list.add(BitMapUtils.combineCenterBitmapDescriptor(bitmap, 0f));
        list.add(BitMapUtils.combineCenterBitmapDescriptor(bitmap, 0.2f));
        list.add(BitMapUtils.combineCenterBitmapDescriptor(bitmap, 0.4f));
        list.add(BitMapUtils.combineCenterBitmapDescriptor(bitmap, 0.6f));
        list.add(BitMapUtils.combineCenterBitmapDescriptor(bitmap, 0.8f));
        list.add(BitMapUtils.combineCenterBitmapDescriptor(bitmap, 0.9f));
        return list;

    }

    public static int[] getImageWidthHeight(Resources res, @DrawableRes int id) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeResource(res,id, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        return new int[]{options.outWidth, options.outHeight};
    }

}

