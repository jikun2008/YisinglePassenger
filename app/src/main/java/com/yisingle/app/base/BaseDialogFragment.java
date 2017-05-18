package com.yisingle.app.base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jikun on 17/3/24.
 */

@SuppressWarnings("unused")
public abstract class BaseDialogFragment extends DialogFragment {
    private Unbinder unbinder;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        if (getContentViewLayoutID() != 0) {
            view = inflater.inflate(getContentViewLayoutID(), container, false);

        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        initViews(savedInstanceState);
    }

    /**
     * bind layout resource file
     *
     * @return id of layout resource
     */
    protected abstract int getContentViewLayoutID();


    /**
     * init all views and add events
     */
    protected abstract void initViews(Bundle savedInstanceState);

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

    }

    private void init() {
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);// 设置Dialog为无标题模式
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置Dialog为无标题模式
        Window window = getDialog().getWindow();
        if (null != window) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// 隐藏软键盘
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 设置Dialog背景色为透明
        }
    }


    /**
     * @param isCancelable false 返回键和dialog外部点击  dialog都不消失
     *                     dialog.setCanceledOnTouchOutside(false);
     *                     dialog弹出后会点击屏幕，dialog不消失；点击物理返回键dialog消失
     */
    protected void setCancelableBackAndScreen(boolean isCancelable) {

        setCancelable(isCancelable);
    }


    /**
     * @param isCancelable fasle返回键 只有点击返回键dialog会消失(点击屏幕不消失)
     */

    protected void setCancelableBack(boolean isCancelable) {

        getDialog().setCanceledOnTouchOutside(isCancelable); //
    }

}
