package com.yisingle.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yisingle.app.R;
import com.yisingle.app.base.Constant;
import com.yisingle.app.data.UserData;
import com.yisingle.app.event.UserDataEvent;
import com.yisingle.app.mvp.IRegister;
import com.yisingle.app.mvp.presenter.RegisterPresenterImpl;
import com.yisingle.baselibray.base.BaseDialogFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jikun on 17/7/31.
 */

public class LoginRegisterDialogFragment extends BaseDialogFragment<RegisterPresenterImpl> implements IRegister.RegisterView {

    @BindView(R.id.tv_info)
    TextView tv_info;


    @BindView(R.id.et_phoneNum_Name)
    EditText et_phoneNum_Name;


    @BindView(R.id.et_password)
    EditText et_password;

    @BindView(R.id.bt_goto_register)
    Button bt_goto_register;

    @BindView(R.id.bt_register)
    Button bt_register;

    @BindView(R.id.bt_login)
    Button bt_login;

    @BindView(R.id.et_confim_password)
    EditText et_confim_password;


    @BindView(R.id.confim_line_view)
    View confim_line_view;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.dialog_fragment_login_register;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {


        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                showInputMethodManager(et_phoneNum_Name);
            }
        };
        handler.sendEmptyMessageDelayed(0, 300);

    }


    private void showInputMethodManager(EditText editText) {
        editText.setFocusable(true);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }

    @Override
    protected RegisterPresenterImpl createPresenter() {
        return new RegisterPresenterImpl(this);
    }

    @Override
    protected boolean isregisterEventBus() {
        return false;
    }


    @OnClick(R.id.ib_close)
    public void closeDiaglog() {
        dismissAllowingStateLoss();
    }

    @OnClick(R.id.bt_goto_register)
    public void dotoRegister() {

        showLoginOrRegisterView(false);
    }

    @OnClick(R.id.bt_login)
    public void doClickLogin() {

        String phoneNum = et_phoneNum_Name.getText().toString();

        String password = et_password.getText().toString();

        if (TextUtils.isEmpty(phoneNum)) {
            ToastUtils.showShort("请输入账号");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ToastUtils.showShort("请输入密码");
            return;
        }

        if (!RegexUtils.isMobileExact(phoneNum)) {
            ToastUtils.showShort("请输入正确的手机号码");
            return;
        }

        mPresenter.login(phoneNum, password, RegisterPresenterImpl.TYPE.Login);
    }

    @OnClick(R.id.bt_register)
    public void doClickRegister() {


        String phone = et_phoneNum_Name.getText().toString();

        String password = et_password.getText().toString();

        String confimpassword = et_confim_password.getText().toString();


        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort("请输入账号");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ToastUtils.showShort("请输入密码");
            return;
        }

        if (TextUtils.isEmpty(confimpassword)) {
            ToastUtils.showShort("请输入确认密码");
            return;
        }


        if (!password.equals(confimpassword)) {
            ToastUtils.showShort("两次密码不一致，请重新输入");
            return;
        }


        if (!RegexUtils.isMobileExact(phone)) {
            ToastUtils.showShort("请输入正确的手机号码");
            return;
        }
        mPresenter.register(phone, password, RegisterPresenterImpl.TYPE.Register);

    }


    /**
     * true到登陆界面 false到注册界面
     *
     * @param isLogin
     */
    private void showLoginOrRegisterView(boolean isLogin) {

        tv_info.setText(isLogin ? "登陆" : "注册");
        bt_goto_register.setVisibility(isLogin ? View.VISIBLE : View.GONE);
        bt_register.setVisibility(isLogin ? View.GONE : View.VISIBLE);
        bt_login.setVisibility(isLogin ? View.VISIBLE : View.GONE);
        et_confim_password.setVisibility(isLogin ? View.GONE : View.VISIBLE);
        confim_line_view.setVisibility(isLogin ? View.GONE : View.VISIBLE);

    }


    @Override
    public void onError(int type) {
        switch (type) {
            case RegisterPresenterImpl.TYPE.Register:
                break;
            case RegisterPresenterImpl.TYPE.Login:
                break;
        }
    }

    @Override
    public void registerSuccess(UserData data) {
        showLoginOrRegisterView(true);
        ToastUtils.showShort("注册成功");

    }

    @Override
    public void loginSuccess(UserData data) {
        dismissAllowingStateLoss();
        ToastUtils.showShort("登陆成功");
        SPUtils.getInstance().put(Constant.LOGIN_PASSENGER_ID, data.getId());
        SPUtils.getInstance().put(Constant.PHONE_NUM, data.getPhonenum());
        SPUtils.getInstance().put(Constant.PASS_WORD, data.getPassword());
        SPUtils.getInstance().put(Constant.IS_LOGIN_SUCCESS, true);
        EventBus.getDefault().post(new UserDataEvent(data));

    }
}
