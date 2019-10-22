package com.chatting.mvplibrary.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.chatting.mvplibrary.Contract.IPresenterContract;
import com.chatting.mvplibrary.Contract.IViewContract;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * author : fengzhangwei
 * date : 2019/9/17
 */
public abstract class MvpFragment<P extends IPresenterContract> extends Fragment implements IBaseView<P>, IViewContract {
    private P mPresenter;

    protected P getPresenter() {
        return mPresenter;
    }

    private void initPresenter() {

        try {
            Class<? extends P> pClass = registerPresenter();
            Constructor<? extends P> constructor = pClass.getConstructor();
            mPresenter = constructor.newInstance();
            mPresenter.registerMvpView(this);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
        mPresenter.onCreate();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
