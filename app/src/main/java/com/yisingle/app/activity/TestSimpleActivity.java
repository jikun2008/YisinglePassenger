package com.yisingle.app.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.yisingle.app.R;
import com.yisingle.app.map.help.AMapLocationHelper;

@SuppressLint("Registered")
public class TestSimpleActivity extends AppCompatActivity {

    private AMapLocationHelper aMapLocationHelper;

    private AMapLocationHelper timeaMapLocationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_simple);
        aMapLocationHelper = new AMapLocationHelper(getApplicationContext());
        timeaMapLocationHelper = new AMapLocationHelper(getApplicationContext());
        timeaMapLocationHelper.startLocation();
        timeaMapLocationHelper.setOnLocationGetListener(new AMapLocationHelper.OnLocationGetListener() {
            @Override
            public void onLocationGetSuccess(AMapLocation loc) {
                Log.e("测试代码", "TestSimpleActivity--time--onLocationGetSuccess.....time");
            }

            @Override
            public void onLocationGetFail(AMapLocation loc) {
                Log.e("测试代码", "TestSimpleActivity--time--onLocationGetFail.....time");
            }
        });
    }

    public void testLocation(View view) {
        aMapLocationHelper.startSingleLocate(new AMapLocationHelper.OnLocationGetListener() {
            @Override
            public void onLocationGetSuccess(AMapLocation loc) {
                Log.e("测试代码", "TestSimpleActivity--single--onLocationGetSuccess");
            }

            @Override
            public void onLocationGetFail(AMapLocation loc) {
                Log.e("测试代码", "TestSimpleActivity--single--onLocationGetFail");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        aMapLocationHelper.destroyLocation();
        timeaMapLocationHelper.destroyLocation();
    }
}
