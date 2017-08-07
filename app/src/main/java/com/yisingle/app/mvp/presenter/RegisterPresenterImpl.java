package com.yisingle.app.mvp.presenter;

import android.support.annotation.IntDef;

import com.yisingle.app.data.UserData;
import com.yisingle.app.http.ApiService;
import com.yisingle.app.http.RetrofitManager;
import com.yisingle.app.mvp.IRegister;
import com.yisingle.app.rx.ApiSubscriber;
import com.yisingle.app.rx.RxUtils;
import com.yisingle.baselibray.base.BasePresenter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jikun on 17/7/31.
 */

public class RegisterPresenterImpl extends BasePresenter<IRegister.RegisterView> implements IRegister.RegisterPresenter {
    public RegisterPresenterImpl(IRegister.RegisterView view) {
        super(view);
    }


    @Override
    public void register(String phone, String password, int type) {

        Map<String, String> params = new HashMap<>();
        params.put("username", phone);
        params.put("password", password);
        params.put("phonenum", phone);

        RetrofitManager.getInstance().createService(ApiService.class).registerPassener(params)
                .compose(RxUtils.apiChildTransformer())
                .subscribe(new ApiSubscriber<UserData>(mView, type) {
                    @Override
                    public void onNext(UserData data) {
                        mView.registerSuccess(data);
                    }
                });

    }

    @Override
    public void login(String phone, String password, int type) {


        Map<String, String> params = new HashMap<>();
        params.put("username", phone);
        params.put("password", password);
        params.put("phonenum", phone);

        RetrofitManager.getInstance().createService(ApiService.class).loginPassener(params)
                .compose(RxUtils.apiChildTransformer())
                .subscribe(new ApiSubscriber<UserData>(mView, type) {
                    @Override
                    public void onNext(UserData data) {
                        mView.loginSuccess(data);
                    }
                });
    }

    //添加支持注解的依赖到你的项目中，需要在build.gradle文件中的依赖块中添加：
    //dependencies { compile 'com.android.support:support-annotations:24.2.0' }
    @IntDef({TYPE.Register, TYPE.Login})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TYPE {

        int Register = 0;
        int Login = 1;

    }
}
