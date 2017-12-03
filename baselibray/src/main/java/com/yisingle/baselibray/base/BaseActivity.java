package com.yisingle.baselibray.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by jikun on 17/5/4.
 */

public abstract class BaseActivity<P extends BasePresenter> extends MySupportActivity implements BaseView {


    protected P mPresenter;
    private Unbinder butterKnife;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());

        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }

        butterKnife = ButterKnife.bind(this);
        mPresenter = createPresenter();
        if (isregisterEventBus()) {
            EventBus.getDefault().register(this);
        }
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


    protected abstract boolean isregisterEventBus();


    protected abstract P createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (null != mPresenter) {
            mPresenter.onDestory();
        }
        butterKnife.unbind();

    }


    @Override
    public void onError(int type) {

    }

    @Override
    public void toast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(int type) {

        showLoadingDialog();

    }

    @Override
    public void dismissLoading(int type) {

        dimisLoadingDialog();

    }

    protected void showLoadingDialog() {
        if (null == progressDialog) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("加载中");
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

    }

    protected void dimisLoadingDialog() {
        if (null != progressDialog && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }





//    /**
//     * @param title    标题
//     * @param backFlag 是否显示返回按钮
//     */
//    @SuppressWarnings("unused")
//    protected void setTitle(String title, boolean backFlag) {
//        if (findViewById(R.id.titleBar) == null) return;
//        ((TextView) findViewById(R.id.tv_title)).setText(title);
//        if (backFlag) {
//            findViewById(R.id.ib_left).setOnClickListener(view -> finish());
//        } else {
//            findViewById(R.id.ib_right).setVisibility(View.GONE);
//        }
//    }
//
//
//    /**
//     * @param title               标题
//     * @param leftOnclickListener 点击监听器
//     */
//    protected void setTitle(String title, View.OnClickListener leftOnclickListener) {
//        if (findViewById(R.id.titleBar) == null) return;
//        ((TextView) findViewById(R.id.tv_title)).setText(title);
//
//        findViewById(R.id.ib_left).setVisibility(View.VISIBLE);
//        findViewById(R.id.ib_left).setOnClickListener(v -> {
//            if (leftOnclickListener != null) {
//                leftOnclickListener.onClick(v);
//            }
//        });
//
//    }


}
