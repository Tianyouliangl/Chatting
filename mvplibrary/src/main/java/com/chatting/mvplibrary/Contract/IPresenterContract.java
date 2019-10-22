package com.chatting.mvplibrary.Contract;

import com.chatting.mvplibrary.Contract.IViewContract;

/**
 * author : fzw
 * date : 2019/9/17
 * 公共Presenter层契约接口 */
public interface IPresenterContract {

    void onCreate();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void registerMvpView(IViewContract mvpView);
}
