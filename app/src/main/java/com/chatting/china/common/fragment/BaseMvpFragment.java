package com.chatting.china.common.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chatting.mvplibrary.Contract.IPresenterContract;
import com.chatting.mvplibrary.view.MvpFragment;


/**
 * author : fengzhangwei
 * date : 2019/9/11
 */
public abstract class BaseMvpFragment<P extends IPresenterContract> extends MvpFragment<P> {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public void initData() {

    }

    public void initView(View view) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    public void goActivity(Class cls){
        goActivity(cls,null);
    }

    public void goActivity(Class cls,Bundle bundle){
        Intent intent = new Intent(getActivity(), cls);
        if (bundle != null){
            intent.putExtra("bundle",bundle);
        }
        this.startActivity(intent);
    }

    protected abstract int getLayoutId();

    private void init(Bundle savedInstanceState) {

    }
}
