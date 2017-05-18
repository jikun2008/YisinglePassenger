package com.yisingle.app.map.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;

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
        Log.e("测试代码", "测试代码radius=" + radius);

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
}
