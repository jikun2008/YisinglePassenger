package com.yisingle.app.dialog;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yisingle.app.R;
import com.yisingle.app.base.BaseDialogFragment;
import com.yisingle.app.base.BasePresenter;

import butterknife.BindView;

/**
 * Created by jikun on 17/5/23.
 */

public class LocationNameQueryDialogFragment extends BaseDialogFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.dialog_fragment_location_name_query;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected boolean isregisterEventBus() {
        return false;
    }


    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
