package com.chatting.china.presenter.main;


import android.support.annotation.NonNull;

import com.blankj.utilcode.util.SPUtils;
import com.chatting.china.common.Constant;
import com.chatting.china.common.ex.BaseObserver;
import com.chatting.china.contract.LoginContract;
import com.chatting.china.domain.LoginBean;
import com.chatting.china.presenter.base.BaseMvpPresenter;
import com.chatting.china.protocol.IHttpProtocol;

import net.ljb.kt.client.HttpFactory;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * author : fengzhangwei
 * date : 2019/9/11
 */
public class LoginPresenter extends BaseMvpPresenter<LoginContract.IView> implements LoginContract.IPresenter {

    @Override
    public void login() {
        String phone = getMvpView().getPhone();
        String password = getMvpView().getPassword();
        if (phone.isEmpty() || password.isEmpty()) {
            getMvpView().loginError("账号或密码不能为空");
            getMvpView().dismissLoading();
            return;
        }
        getMvpView().showLoading();
        loginNet(phone, password);
    }

    private void loginNet(final String phone, final String password) {
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("password", password);
        HttpFactory.INSTANCE.getProtocol(IHttpProtocol.class)
                .login(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<LoginBean>(getMvpView()){
                    @Override
                    protected void onNextEx(@NonNull LoginBean data) {
                        SPUtils.getInstance().put(Constant.USER_PHONE,phone);
                        SPUtils.getInstance().put(Constant.USER_PASSWORD,password);
                        getMvpView().loginSuccess();
                    }

                    @Override
                    protected void onNextSN(String msg) {
                        getMvpView().loginError(msg);
                    }

                    @Override
                    protected void onErrorEx(@NonNull Throwable e) {
                        if (e.toString().contains("ConnectException")){
                            if (e.toString().contains("Failed to connect to")){
                                getMvpView().loginError("连接服务器失败!");
                            }else {
                                getMvpView().loginError("请检查网络连接!");
                            }
                        }else {
                            getMvpView().loginError(e.toString());
                        }
                    }
                });
    }
}
