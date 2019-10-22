package com.chatting.china.presenter.base;


import com.chatting.mvplibrary.Contract.IPresenterContract;
import com.chatting.mvplibrary.Contract.IViewContract;
import com.chatting.mvplibrary.presenter.IBasePresenter;

/**
 * author : fengzhangwei
 * date : 2019/9/11
 */
public abstract class BaseMvpPresenter<V extends IViewContract> implements IBasePresenter<V>, IPresenterContract {
    private V mMvpView = null;

    @Override
    public void registerMvpView(IViewContract mvpView) {
            mMvpView = (V)mvpView;
        }

    @Override
    public V getMvpView() {
        return mMvpView;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }
}
