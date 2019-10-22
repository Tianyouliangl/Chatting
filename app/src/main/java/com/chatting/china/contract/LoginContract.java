package com.chatting.china.contract;


import com.chatting.mvplibrary.Contract.IPresenterContract;
import com.chatting.mvplibrary.Contract.IViewContract;

/**
 * author : fengzhangwei
 * date : 2019/9/11
 */
public interface LoginContract {

    interface IView extends IViewContract {

        void loginSuccess();

        void loginError(String msg);

        String getPhone();

        String getPassword();

    }

    interface IPresenter extends IPresenterContract {

        void login();

    }
}
