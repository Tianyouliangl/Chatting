package com.test.project.utils

import android.app.Activity
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import cn.nekocode.rxlifecycle.LifecycleEvent
import cn.nekocode.rxlifecycle.RxLifecycle
import cn.nekocode.rxlifecycle.compact.RxLifecycleCompact
import com.chatting.china.fragment.DynamicFragment
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.Disposable

/**
 * Author:Ljb
 * Time:2018/12/28
 * There is a lot of misery in life
 **/
object RxUtils {


    fun dispose(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

    /**
     * Java使用该方法bind Rx生命周期
     * Kotlin可直接调用bindRxLife()绑定我们自己实现的生命周期管理
     * */
    fun <T> bindToLifecycle(obj: Any): ObservableTransformer<T, T> {
        return if (obj is AppCompatActivity) {
            RxLifecycleCompact.bind(obj).disposeObservableWhen(LifecycleEvent.DESTROY_VIEW)
        } else if (obj is FragmentActivity) {
            RxLifecycle.bind(obj).disposeObservableWhen(LifecycleEvent.DESTROY_VIEW)
        } else if (obj is Activity) {
            RxLifecycle.bind(obj).disposeObservableWhen(LifecycleEvent.DESTROY_VIEW)
        } else if (obj is Fragment) {
            RxLifecycleCompact.bind(obj).disposeObservableWhen(LifecycleEvent.DESTROY_VIEW)
        } else {
            throw IllegalArgumentException("view isn't activity or fragment")
        }
    }

}