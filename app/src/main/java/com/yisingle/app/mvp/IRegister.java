package com.yisingle.app.mvp;


import com.yisingle.app.base.BaseView;
import com.yisingle.app.data.ChoosePointData;
import com.yisingle.app.data.UserData;

/**
 * 登录页接口
 * Created by yu on 2016/11/2.
 */
public interface IRegister {
    interface RegisterView extends BaseView {

        void registerSuccess(UserData choosePointData);

        void loginSuccess(UserData choosePointData);
    }

    interface RegisterPresenter {


        void register(String phone, String password,int type);


        void login(String phone, String password,int type);

    }

}