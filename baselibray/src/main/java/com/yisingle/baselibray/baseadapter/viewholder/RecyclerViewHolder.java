package com.yisingle.baselibray.baseadapter.viewholder;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.yisingle.baselibray.R;

/**
 * 适用于RecyclerView的ViewHolder
 */
@SuppressWarnings({"unused", "CanBeFinal", "UnusedReturnValue"})
public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    /**
     * ViewHolder实现类,桥接模式适配AbsListView与RecyclerView的二维变化
     */
    ViewHolderImpl mHolderImpl;

    /**
     * @param itemView itemView
     */
    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mHolderImpl = new ViewHolderImpl(itemView);
    }

    public Context getContext() {
        return mHolderImpl.getItemView().getContext();
    }

    /**
     * @param viewId viewId
     * @param <T>    T
     * @return T extends View
     */
    public <T extends View> T get(int viewId) {
        return mHolderImpl.findViewById(viewId);
    }

    public View getItemView() {
        return mHolderImpl.getItemView();
    }

    public RecyclerViewHolder setText(int viewId, int stringId) {
        mHolderImpl.setText(viewId, stringId);
        return this;
    }

    public RecyclerViewHolder setText(int viewId, CharSequence text) {
        mHolderImpl.setText(viewId, text);
        return this;
    }

    public RecyclerViewHolder setTextColor(int viewId, int color) {
        mHolderImpl.setTextColor(viewId, color);
        return this;
    }

    public RecyclerViewHolder setTextColor(int viewId, ColorStateList colors ) {
        mHolderImpl.setTextColor(viewId, colors);
        return this;
    }

    /**
     * @param viewId viewId
     * @param color  color
     */
    public RecyclerViewHolder setBackgroundColor(int viewId, int color) {
        mHolderImpl.setBackgroundColor(viewId, color);
        return this;
    }

    public RecyclerViewHolder setBackgroundResource(int viewId, int resId) {
        mHolderImpl.setBackgroundResource(viewId, resId);
        return this;
    }

    public RecyclerViewHolder setBackgroundDrawable(int viewId, Drawable drawable) {
        mHolderImpl.setBackgroundDrawable(viewId, drawable);
        return this;
    }

    @TargetApi(16)
    public RecyclerViewHolder setBackground(int viewId, Drawable drawable) {
        mHolderImpl.setBackground(viewId, drawable);
        return this;
    }

    public RecyclerViewHolder setImageUrl(Context aty, int viewId, String url) {
        mHolderImpl.setImageUrl(aty, viewId, url);
        return this;
    }

    public RecyclerViewHolder setImageUrl(Fragment fmt, int viewId, String url) {
        mHolderImpl.setImageUrl(fmt, viewId, url);
        return this;
    }

    public RecyclerViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        mHolderImpl.setImageBitmap(viewId, bitmap);
        return this;
    }


    public RecyclerViewHolder setImageResource(int viewId, int resId) {
        mHolderImpl.setImageResource(viewId, resId);
        return this;
    }

    public RecyclerViewHolder setImageDrawable(int viewId, Drawable drawable) {
        mHolderImpl.setImageDrawable(viewId, drawable);
        return this;
    }


    public RecyclerViewHolder setImageDrawable(int viewId, Uri uri) {
        mHolderImpl.setImageDrawable(viewId, uri);
        return this;
    }


    @TargetApi(16)
    public RecyclerViewHolder setImageAlpha(int viewId, int alpha) {
        mHolderImpl.setImageAlpha(viewId, alpha);
        return this;
    }


    public RecyclerViewHolder setDrawableLeft(int viewId, Drawable drawable) {
        mHolderImpl.setDrawableLeft(viewId, drawable); //设置左图标
        return this;
    }

    public RecyclerViewHolder setChecked(int viewId, boolean checked) {
        mHolderImpl.setChecked(viewId, checked);
        return this;
    }


    public RecyclerViewHolder setProgress(int viewId, int progress) {
        mHolderImpl.setProgress(viewId, progress);
        return this;
    }

    public RecyclerViewHolder setProgress(int viewId, int progress, int max) {
        mHolderImpl.setProgress(viewId, progress, max);
        return this;
    }

    public RecyclerViewHolder setMax(int viewId, int max) {
        mHolderImpl.setMax(viewId, max);
        return this;
    }

    public RecyclerViewHolder setRating(int viewId, float rating) {
        mHolderImpl.setRating(viewId, rating);
        return this;
    }

    public RecyclerViewHolder setRating(int viewId, float rating, int max) {
        mHolderImpl.setRating(viewId, rating, max);
        return this;
    }

    public RecyclerViewHolder setSelected(int viewId, boolean isSelected) {
        mHolderImpl.setSelected(viewId, isSelected);
        return this;
    }

    public RecyclerViewHolder setVisibility(int viewId, int visibility) {
        mHolderImpl.setVisibility(viewId, visibility);
        return this;
    }

    public RecyclerViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        mHolderImpl.setOnClickListener(viewId, listener);
        return this;
    }

    public RecyclerViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        mHolderImpl.setOnTouchListener(viewId, listener);
        return this;
    }


    public RecyclerViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        mHolderImpl.setOnLongClickListener(viewId, listener);
        return this;
    }

    public RecyclerViewHolder setOnItemClickListener(int viewId, AdapterView.OnItemClickListener listener) {
        mHolderImpl.setOnItemClickListener(viewId, listener);
        return this;
    }


    public RecyclerViewHolder setOnItemLongClickListener(int viewId, AdapterView.OnItemLongClickListener listener) {
        mHolderImpl.setOnItemLongClickListener(viewId, listener);
        return this;
    }


    public RecyclerViewHolder setOnItemSelectedClickListener(int viewId, AdapterView.OnItemSelectedListener listener) {
        mHolderImpl.setOnItemSelectedClickListener(viewId, listener);
        return this;
    }
}
