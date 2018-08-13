package com.yisingle.app.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.yisingle.app.R;
import com.yisingle.app.service.LocationService;
import com.yisingle.baselibray.base.BaseActivity;
import com.yisingle.baselibray.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jikun on 17/8/3.
 */

public class TestActivity extends BaseActivity {
    @Override
    protected int getContentViewLayoutID() {
        return R.layout.actiivity_test;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        getPermission();
    }

    @Override
    protected boolean isregisterEventBus() {
        return false;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    public void start(View view) {
        LocationService.startService(getApplicationContext());
    }

    public void stop(View view) {
        LocationService.stopService(getApplicationContext());
    }


    //定位权限管理

    private void getPermission() {
        AndPermission.with(this)
                .requestCode(300)
                .permission(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .rationale((requestCode, rationale) -> {
                    // 此对话框可以自定义，调用rationale.resume()就可以继续申请。
                    AndPermission.rationaleDialog(this, rationale).show();

                })
                .callback(this)
                .start();
    }

    // 成功回调的方法，用注解即可，这里的300就是请求时的requestCode。
    @PermissionYes(300)
    private void getPermissionYes(List<String> grantedPermissions) {
        // TODO 申请权限成功。

    }

    @PermissionNo(300)
    private void getPermissionNo(List<String> deniedPermissions) {
        // TODO 申请权限失败。


        // 是否有不再提示并拒绝的权限。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            // 第一种：用AndPermission默认的提示语。
            AndPermission.defaultSettingDialog(this, 400).show();

        } else {
            ToastUtils.showShort("你拒绝了定位权限请重新启动应用并允许定位权限");

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<String> permissionList = new ArrayList<>();
        permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionList.add(Manifest.permission.CALL_PHONE);
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        switch (requestCode) {
            case 400:  // 这个400就是你上面传入的数字。
                // 你可以在这里检查你需要的权限是否被允许，并做相应的操作。
                if (AndPermission.hasPermission(this, permissionList)) {

                } else {
                    ToastUtils.showShort("你拒绝了定位权限请重新启动应用并允许定位权限");

                }
                break;
            default:
                break;
        }
    }
}
