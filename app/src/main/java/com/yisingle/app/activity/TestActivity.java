package com.yisingle.app.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.transition.TransitionManager;
import android.view.View;

import com.yisingle.app.R;
import com.yisingle.app.R2;
import com.yisingle.app.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by jikun on 17/5/9.
 */


public class TestActivity extends BaseActivity {

    @BindView(R.id.main)
    public ConstraintLayout constraintLayout;
    private ConstraintSet applyConstraintSet = new ConstraintSet();
    private ConstraintSet resetConstraintSet = new ConstraintSet();


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.actiivity_test;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        resetConstraintSet.clone(constraintLayout);
        applyConstraintSet.clone(constraintLayout);

    }

    public void onApplyClick(View view) {
        TransitionManager.beginDelayedTransition(constraintLayout);
        applyConstraintSet.setMargin(R.id.button1, ConstraintSet.START, 0);
        applyConstraintSet.applyTo(constraintLayout);

    }

    public void onResetClick(View view) {
        TransitionManager.beginDelayedTransition(constraintLayout);
        resetConstraintSet.applyTo(constraintLayout);

    }
}
