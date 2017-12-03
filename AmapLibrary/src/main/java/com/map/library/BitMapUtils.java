package com.map.library;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.support.annotation.DrawableRes;


/**
 * Created by jikun on 17/7/4.
 */

public class BitMapUtils {

    public static Bitmap getHalfBitMap(Resources resources, @DrawableRes int drawableId) {


        Bitmap bitmap = BitmapFactory.decodeResource(resources, drawableId);
        Bitmap newbitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight() / 2);
        return newbitmap;

    }
}
