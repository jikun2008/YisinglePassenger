package com.yisingle.app.base;

import android.view.View;
import android.widget.TextView;

import com.map.library.base.BaseMapActivity;
import com.yisingle.app.R;
import com.yisingle.baselibray.base.BasePresenter;

/**
 * Created by jikun on 17/8/2.
 */

public abstract class BasePassengerMapActivity<P extends BasePresenter> extends BaseMapActivity<P> {


    /**
     * @param title    标题
     * @param backFlag 是否显示返回按钮
     */
    @SuppressWarnings("unused")
    protected void setTitle(String title, boolean backFlag) {
        if (findViewById(R.id.titleBar) == null) {
            return;
        }
        ((TextView) findViewById(R.id.tv_title)).setText(title);
        if (backFlag) {
            findViewById(R.id.ib_left).setOnClickListener(view -> finish());
        } else {
            findViewById(R.id.ib_right).setVisibility(View.GONE);
        }
    }


    /**
     * @param title               标题
     * @param leftOnclickListener 点击监听器
     */
    protected void setTitle(String title, View.OnClickListener leftOnclickListener) {
        if (findViewById(R.id.titleBar) == null) {
            return;
        }
        ((TextView) findViewById(R.id.tv_title)).setText(title);

        findViewById(R.id.ib_left).setVisibility(View.VISIBLE);
        findViewById(R.id.ib_left).setOnClickListener(v -> {
            if (leftOnclickListener != null) {
                leftOnclickListener.onClick(v);
            }
        });

    }

}
