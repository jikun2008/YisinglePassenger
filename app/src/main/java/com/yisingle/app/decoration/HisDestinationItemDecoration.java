package com.yisingle.app.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yisingle.app.R;

public class HisDestinationItemDecoration extends RecyclerView.ItemDecoration {

    private int dividerHeight;
    private Paint paint;
    private int graycolor;

    public HisDestinationItemDecoration(Context context, int dividerHeight) {
        paint = new Paint();
        graycolor = ContextCompat.getColor(context, R.color.hint_grey_text);
        paint.setColor(graycolor);
        this.dividerHeight = dividerHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        if (pos == 0) {
            outRect.set(0, 0, dividerHeight, 0);
        } else if (pos == 1) {
            outRect.set(0, 0, 0, 0);
        } else {
            outRect.set(0, dividerHeight, 0, 0);
        }
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        canvas.save();

        float left;
        float right;
        float top;
        float bottom;
        int childCount = parent.getChildCount();
        View childView;
        for (int i = 0; i < childCount; i++) {
            childView = parent.getChildAt(i);
            if (i == 0) {
                paint.setColor(graycolor);
                top = childView.getTop();
                bottom = childView.getBottom();
                left = childView.getRight();
                right = left + dividerHeight;
                canvas.drawRect(left, top, right, bottom, paint);//绘制横向间隔
            } else if (i == 1) {

            } else {
                top = childView.getTop() - dividerHeight;
                bottom = childView.getTop();
                paint.setColor(graycolor);
                left = childView.getLeft() - childView.getPaddingStart();
                right = childView.getRight() - childView.getPaddingEnd();
                canvas.drawRect(left, top, right, bottom, paint);
            }
        }
        canvas.restore();
    }
}
