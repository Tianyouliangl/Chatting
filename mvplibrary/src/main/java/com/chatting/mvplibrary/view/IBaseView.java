package com.chatting.mvplibrary.view;

import com.chatting.mvplibrary.Contract.IPresenterContract;

/**
 * author : fengzhangwei
 * date : 2019/9/17
 */
public interface IBaseView <P extends IPresenterContract>{
    Class<? extends P> registerPresenter();
}
