package com.yisingle.baselibray.base;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jikun on 17/3/24.
 */

public abstract class BaseDialogFragment<P extends BasePresenter> extends DialogFragment implements BaseView {

    protected P mPresenter;
    private Unbinder butterKnife;


    private ProgressDialog progressDialog;


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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDialog();
        butterKnife = ButterKnife.bind(this, view);
        mPresenter = createPresenter();
        initViews(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isregisterEventBus()) {
            EventBus.getDefault().register(this);
        }

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


    protected abstract P createPresenter();


    protected abstract boolean isregisterEventBus();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (null != mPresenter) {
            mPresenter.onDestory();
        }
        butterKnife.unbind();

    }

    /**
     * 初始化一些Dialog信息
     */
    protected void initDialog() {
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);// 设置Dialog为无标题模式
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置Dialog为无标题模式
        Window window = getDialog().getWindow();
        if (null != window) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// 隐藏软键盘
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 设置Dialog背景色为透明
        }


    }

    private void test() {
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
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

    @Override
    public void onError(int type) {

    }

    @Override
    public void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(int type) {
        showLoadingDialog();
    }

    @Override
    public void dismissLoading(int type) {
        dimisLoadingDialog();
    }


    private void showLoadingDialog() {
        if (null == progressDialog) {
            progressDialog = new ProgressDialog(getActivity());
        }
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("加载中");
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

    }

    private void dimisLoadingDialog() {
        if (null != progressDialog && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

}
