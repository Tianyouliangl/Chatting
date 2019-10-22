package com.chatting.china.common.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

/**
 * author : fengzhangwei
 * date : 2019/9/11
 */
public abstract class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        init(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {

    }

    private void init(Bundle savedInstanceState) {

    }

    protected void goActivity(Class cls){
        goActivity(cls,null);
    }

    protected void goActivity(Class cls,Bundle bundle){
        Intent intent = new Intent(this, cls);
        if (bundle != null){
            intent.putExtra("bundle",bundle);
        }
        startActivity(intent);
    }


    protected abstract int getLayoutId();
}
