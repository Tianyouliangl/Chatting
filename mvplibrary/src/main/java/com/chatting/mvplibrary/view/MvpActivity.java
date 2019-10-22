package com.chatting.mvplibrary.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.chatting.mvplibrary.Contract.IPresenterContract;
import com.chatting.mvplibrary.Contract.IViewContract;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * author : fengzhangwei
 * date : 2019/9/17
 */
public abstract class MvpActivity<P extends IPresenterContract> extends Activity implements IBaseView<P>, IViewContract {

    private P mPresenter = null;

    protected P getPresenter() {
        return mPresenter;
    }


    private void initPresenter() {
        try {
            Class<? extends P> pClass = registerPresenter();
            Constructor<? extends P> constructor = pClass.getConstructor();
            mPresenter = constructor.newInstance();
            if (mPresenter != null) {
                mPresenter.registerMvpView(this);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.i("MVP", e.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.i("MVP", e.toString());
        } catch (InstantiationException e) {
            e.printStackTrace();
            Log.i("MVP", e.toString());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Log.i("MVP", e.toString());
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
        if (mPresenter != null) {
            mPresenter.onCreate();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPresenter != null) {
            mPresenter.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }
}
