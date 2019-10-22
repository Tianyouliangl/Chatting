package com.chatting.china.act;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.SPUtils;
import com.chatting.china.R;
import com.chatting.china.common.Constant;
import com.chatting.china.common.act.BaseMvpActivity;
import com.chatting.china.contract.LoginContract;
import com.chatting.china.presenter.main.LoginPresenter;
import com.senyint.im.socket.SocketManager;


import butterknife.BindView;

/**
 * author : fengzhangwei
 * date : 2019/9/11
 */
public class LoginActivity extends BaseMvpActivity<LoginContract.IPresenter> implements LoginContract.IView, View.OnClickListener {

    @BindView(R.id.ed_mobile)
    EditText mobile;
    @BindView(R.id.ed_pwd)
    EditText password;
    @BindView(R.id.btn_login)
    Button btn_login;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void loginSuccess() {
        goActivity(MainActivity.class);
        finish();
    }

    @Override
    public void loginError(String msg) {
        showToast(msg);
    }

    @Override
    public String getPhone() {
        return mobile.getText().toString();
    }

    @Override
    public String getPassword() {
        return password.getText().toString();
    }

    @Override
    public void initData() {
        String pn = SPUtils.getInstance().getString(Constant.USER_PHONE);
        String pwd = SPUtils.getInstance().getString(Constant.USER_PASSWORD);
        if (!pn.isEmpty() && !pwd.isEmpty()){
            mobile.setText(pn);
            password.setText(pwd);
            getPresenter().login();
        }
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            getPresenter().login();
            loginSocket();
        }
    }

    private void loginSocket() {
        String token = SocketManager.INSTANCE.getSocketToken(com.senyint.im.listener.Constant.uid,com.senyint.im.listener.Constant.token);
        SocketManager.INSTANCE.loginSocket(getApplicationContext(),token);
    }

    @Override
    public Class<? extends LoginContract.IPresenter> registerPresenter() {
        return LoginPresenter.class;
    }
}
