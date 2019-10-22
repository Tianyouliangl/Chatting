package com.chatting.mvplibrary.presenter;

import com.chatting.mvplibrary.Contract.IViewContract;

/**
 * author : fengzhangwei
 * date : 2019/9/17
 */
public interface IBasePresenter<v extends IViewContract> {
    v getMvpView();
}
