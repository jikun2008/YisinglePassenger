package com.yisingle.app.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yisingle.app.R;
import com.yisingle.app.utils.BitMapUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author jikun
 * Created by jikun on 2018/8/1.
 */
public class CenterChoosPlaceView extends LinearLayout {
    @BindView(R.id.ivCenter)
    ImageView ivCenter;

    @BindView(R.id.infoView)
    View infoView;


    @BindView(R.id.llHaveNet)
    LinearLayout llHaveNet;

    @BindView(R.id.ivLoading)
    ImageView ivLoading;

    @BindView(R.id.llLeft)
    LinearLayout llLeft;

    @BindView(R.id.tvNoHaveNet)
    TextView tvNoHaveNet;

    AnimationDrawable centerDrawable = new AnimationDrawable();


    public CenterChoosPlaceView(Context context) {
        super(context);
        initView(context);
    }

    public CenterChoosPlaceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CenterChoosPlaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_center_marker, null);
        addView(view, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        ButterKnife.bind(this, view);
        for (Drawable drawable : getCenterListDrawable()) {
            centerDrawable.addFrame(drawable, 100);
        }
        centerDrawable.setOneShot(false);

    }


    public void showLoading(@IntRange(from = 1, to = 20) int time) {
        ivCenter.setImageDrawable(centerDrawable);
        showViewByData(CenterData.createLoading());


    }

    public void stopLoading() {
        doAnimation(false);
    }

    public void showSuccess() {
        showViewByData(CenterData.createSuccess());
    }

    public void showError() {
        showViewByData(CenterData.createError());
    }


    public void showInfoWindow() {
        if (null != infoView && infoView.getVisibility() == INVISIBLE) {
            infoView.setVisibility(VISIBLE);

        }
    }

    public void hideInfoWindow() {
        if (null != infoView && infoView.getVisibility() == VISIBLE) {
            infoView.setVisibility(INVISIBLE);

        }
    }


    private void showViewByData(CenterData data) {
        if (data.getType() == CenterData.Type.LOADING) {
            llHaveNet.setVisibility(View.VISIBLE);
            tvNoHaveNet.setVisibility(View.GONE);
            llLeft.setVisibility(View.GONE);
            ivLoading.setVisibility(View.VISIBLE);

            doAnimation(true);

        } else if (data.getType() == CenterData.Type.SUCCESS) {

            llHaveNet.setVisibility(View.VISIBLE);
            tvNoHaveNet.setVisibility(View.GONE);
            llLeft.setVisibility(View.VISIBLE);
            ivLoading.setVisibility(View.GONE);

            doAnimation(false);
        } else {
            Log.e("测试代码", "测试代码bindData------Failed");

            llHaveNet.setVisibility(View.GONE);
            tvNoHaveNet.setVisibility(View.VISIBLE);

            doAnimation(false);
        }
    }

    private void doAnimation(boolean isstart) {

        AnimationDrawable loadingDrawable = (AnimationDrawable) ivLoading.getDrawable();
        if (isstart) {

            if (!loadingDrawable.isRunning()) {
                loadingDrawable.start();
            }

            if (!centerDrawable.isRunning()) {
                centerDrawable.start();
            }


        } else {
            if (loadingDrawable.isRunning()) {
                loadingDrawable.stop();
            }

            if (centerDrawable.isRunning()) {
                centerDrawable.stop();
            }
            ivCenter.setImageDrawable(getCenterDrawable());
        }
    }


    public static class CenterData {
        @Type
        int type;

        /**
         * 添加支持注解的依赖到你的项目中，需要在build.gradle文件中的依赖块中添加：
         * dependencies { compile 'com.android.support:support-annotations:24.2.0' }
         */
        @IntDef({CenterData.Type.LOADING, CenterData.Type.SUCCESS, CenterData.Type.ERROR})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Type {

            int LOADING = 0;
            int SUCCESS = 1;
            int ERROR = 2;


        }

        public static CenterData createLoading() {
            CenterData centerData = new CenterData();
            centerData.setType(Type.LOADING);
            return centerData;
        }

        public static CenterData createSuccess() {
            CenterData centerData = new CenterData();
            centerData.setType(Type.SUCCESS);
            return centerData;
        }

        public static CenterData createError() {
            CenterData centerData = new CenterData();
            centerData.setType(Type.ERROR);
            return centerData;
        }

        public
        @Type
        int getType() {
            return type;
        }

        public void setType(@Type int type) {
            this.type = type;
        }
    }

    private Drawable getCenterDrawable() {
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                R.mipmap.icon_me_location);
        return new BitmapDrawable(getResources(), bitmap);
    }

    private List<Drawable> getCenterListDrawable() {
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                R.mipmap.icon_me_location);
        return BitMapUtils.getMultipleBitmapDescriptorList(getResources(), bitmap);
    }
}
