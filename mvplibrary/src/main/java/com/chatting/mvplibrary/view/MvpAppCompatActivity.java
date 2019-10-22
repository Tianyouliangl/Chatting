package com.chatting.mvplibrary.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.chatting.mvplibrary.Contract.IPresenterContract;
import com.chatting.mvplibrary.Contract.IViewContract;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * author : fengzhangwei
 * date : 2019/9/17
 */
public abstract class MvpAppCompatActivity<P extends IPresenterContract> extends AppCompatActivity implements IBaseView<P>, IViewContract {

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
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
        mPresenter.onCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
