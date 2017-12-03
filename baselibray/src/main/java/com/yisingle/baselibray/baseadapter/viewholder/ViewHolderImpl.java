package com.yisingle.baselibray.baseadapter.viewholder;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;


/**
 * ViewHolder操作子视图的实现类
 */
@SuppressWarnings({"CanBeFinal", "UnusedAssignment", "UnusedParameters", "WeakerAccess"})
public class ViewHolderImpl {

    /**
     * 缓存子视图,key为view id, 值为View。
     */
    private SparseArray<View> mCahceViews = new SparseArray<>();

    /**
     * Item View
     */
    private View mItemView;

    ViewHolderImpl(View itemView) {
        mItemView = itemView;
    }

    public View getItemView() {
        return mItemView;
    }

    /**
     * 根据id查找view
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T findViewById(int viewId) {
        View target = mCahceViews.get(viewId);
        if (target == null) {
            target = mItemView.findViewById(viewId);
            mCahceViews.put(viewId, target);
        }
        return (T) target;
    }

    /**
     * @param viewId   viewId
     * @param stringId stringId
     */
    public void setText(int viewId, int stringId) {
        TextView textView = findViewById(viewId);
        textView.setText(stringId);
    }

    public void setText(int viewId, CharSequence text) {
        TextView textView = findViewById(viewId);
        textView.setText(text);
    }

    public void setTextColor(int viewId, int color) {
        TextView textView = findViewById(viewId);
        textView.setTextColor(color);
    }

    public void setTextColor(int viewId, ColorStateList color) {
        TextView textView = findViewById(viewId);
        textView.setTextColor(color);
    }

    /**
     * @param viewId viewId
     * @param color  color
     */
    public void setBackgroundColor(int viewId, int color) {
        View target = findViewById(viewId);
        target.setBackgroundColor(color);
    }

    public void setBackgroundResource(int viewId, int resId) {
        View target = findViewById(viewId);
        target.setBackgroundResource(resId);
    }

    @SuppressLint("ObsoleteSdkInt")
    @SuppressWarnings("deprecation")
    public void setBackgroundDrawable(int viewId, Drawable drawable) {
        View target = findViewById(viewId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            target.setBackground(drawable);
        } else {
            target.setBackgroundDrawable(drawable);
        }
    }

    @TargetApi(16)
    public void setBackground(int viewId, Drawable drawable) {
        View target = findViewById(viewId);
        target.setBackground(drawable);
    }

    /**
     * @param viewId viewId
     */
    public void setImageUrl(Context aty, int viewId, String url) {
        ImageView target = findViewById(viewId);
        // ImageLoader.load(aty, target, url);
    }

    public void setImageUrl(Fragment fmt, int viewId, String url) {
        ImageView target = findViewById(viewId);
        // ImageLoader.load(fmt, target, url);
    }

    public void setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView target = findViewById(viewId);
        target.setImageBitmap(bitmap);
    }

    public void setImageResource(int viewId, int resId) {
        ImageView target = findViewById(viewId);
        target.setImageResource(resId);
    }

    public void setImageDrawable(int viewId, Drawable drawable) {
        ImageView target = findViewById(viewId);
        target.setImageDrawable(drawable);
    }

    public void setImageDrawable(int viewId, Uri uri) {
        ImageView target = findViewById(viewId);
        target.setImageURI(uri);
    }

    @TargetApi(16)
    public void setImageAlpha(int viewId, int alpha) {
        ImageView target = findViewById(viewId);
        target.setImageAlpha(alpha);
    }

    public void setDrawableLeft(int viewId, Drawable drawable) {

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        View view = findViewById(viewId);
        if (view instanceof TextView) {
            TextView textview = (TextView) view;
            textview.setCompoundDrawables(drawable, null, null, null); //设置左图标
        } else if (view instanceof Button) {
            Button button = (Button) view;
            button.setCompoundDrawables(drawable, null, null, null); //设置左图标
        } else {

        }

    }

    /**
     * @param viewId  viewId
     * @param checked checked
     */
    public void setChecked(int viewId, boolean checked) {
        Checkable checkable = findViewById(viewId);
        checkable.setChecked(checked);
    }

    public void setProgress(int viewId, int progress) {
        ProgressBar view = findViewById(viewId);
        view.setProgress(progress);
    }

    public void setProgress(int viewId, int progress, int max) {
        ProgressBar view = findViewById(viewId);
        view.setMax(max);
        view.setProgress(progress);
    }

    public void setMax(int viewId, int max) {
        ProgressBar view = findViewById(viewId);
        view.setMax(max);
    }

    public void setRating(int viewId, float rating) {
        RatingBar view = findViewById(viewId);
        view.setRating(rating);
    }

    public void setSelected(int viewId, boolean isSelected) {
        View view = findViewById(viewId);
        view.setSelected(isSelected);
    }

    public void setRating(int viewId, float rating, int max) {
        RatingBar view = findViewById(viewId);
        view.setMax(max);
        view.setRating(rating);
    }

    public void setVisibility(int viewId, int visibility) {
        View view = findViewById(viewId);
        view.setVisibility(visibility);
    }

    public void startAnimationDrawable(int viewId) {
        ImageView imageView = findViewById(viewId);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
        if (!animationDrawable.isRunning()) {
            animationDrawable.start();
        }
    }

    public void stopAnimationDrawable(int viewId) {
        ImageView imageView = findViewById(viewId);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
        if (animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }

    /**
     * @param viewId   viewId
     * @param listener listener
     */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = findViewById(viewId);
        view.setOnClickListener(listener);
    }

    public void setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = findViewById(viewId);
        view.setOnTouchListener(listener);
    }

    public void setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = findViewById(viewId);
        view.setOnLongClickListener(listener);
    }

    public void setOnItemClickListener(int viewId, AdapterView.OnItemClickListener listener) {
        AdapterView view = findViewById(viewId);
        view.setOnItemClickListener(listener);
    }

    public void setOnItemLongClickListener(int viewId, AdapterView.OnItemLongClickListener listener) {
        AdapterView view = findViewById(viewId);
        view.setOnItemLongClickListener(listener);
    }

    public void setOnItemSelectedClickListener(int viewId, AdapterView.OnItemSelectedListener listener) {
        AdapterView view = findViewById(viewId);
        view.setOnItemSelectedListener(listener);
    }
}
