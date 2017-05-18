package com.yisingle.app.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * Created by jikun on 17/5/9.
 */


public abstract class BaseFrament extends SupportFragment {


    private Unbinder unbinder;

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

        if (isregisterEventBus()) {
            EventBus.getDefault().register(this);
        }
        unbinder = ButterKnife.bind(this, view);
        initViews(savedInstanceState);


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        unbinder.unbind();
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


}
