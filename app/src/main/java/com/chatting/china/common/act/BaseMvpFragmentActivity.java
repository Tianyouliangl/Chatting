package com.chatting.china.common.act;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chatting.china.R;
import com.chatting.china.widgets.BaseNetLayout;
import com.chatting.china.widgets.dialog.LoadingDialog;
import com.chatting.mvplibrary.Contract.IPresenterContract;
import com.chatting.mvplibrary.view.MvpActivity;
import com.chatting.mvplibrary.view.MvpFragmentActivity;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * author : fengzhangwei
 * date : 2019/9/17
 */
public abstract class BaseMvpFragmentActivity<P extends IPresenterContract> extends MvpFragmentActivity<P> implements NetChangeListener {

    @Nullable
    @BindView(R.id.toolbar_back)
    ImageView toolbar_back;
    @Nullable
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @Nullable
    @BindView(R.id.toolbar_menu_tv)
    TextView toolbar_menu_tv;
    @Nullable
    @BindView(R.id.toolbar_menu)
    ImageView toolbar_menu;
    @Nullable
    @BindView(R.id.base_net)
    BaseNetLayout net_view;
    private LoadingDialog mLoading;
    private Unbinder mBind;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mBind = ButterKnife.bind(this);
        init(savedInstanceState);
        initView();
        initData();
        initBar();
    }

    private void initBar() {
        ImmersionBar.with(this)
                .transparentBar()             //透明状态栏和导航栏，不写默认状态栏为透明色，导航栏为黑色（设置此方法，fullScreen()方法自动为 true）
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR) // 隐藏导航栏或者状态栏
                .flymeOSStatusBarFontColor(R.color.colorWhite)  // 状态栏字体颜色
                .navigationBarColor(R.color.color_8d91a5)    // 导航栏背景颜色
                .statusBarColorTransform(R.color.color_8d91a5)  //状态栏变色后的颜色
                .statusBarAlpha(0.4f)         // 状态栏透明度
                .init();
    }

    public void initData() {
        mLoading = new LoadingDialog(this, false);
        boolean connected = NetBroadcastReceiver.isNetworkConnected(this);
        setNetChange(connected);
    }

    public void initView() {

    }

    public void init(Bundle savedInstanceState) {
        NetBroadcastReceiver.setRegisterNetBRChange(this);
    }

    public void goActivity(Class cls) {
        goActivity(cls, null);
    }

    public void goActivity(Class cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        startActivity(intent);
    }


    @Override
    public void onChangeListener(Boolean status) {
        setNetChange(status);
    }

    private void setNetChange(Boolean status) {
        if (net_view != null){
            net_view.setNetChange(status);
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showLoading() {
        if (mLoading != null && !mLoading.isShowing()) {
            mLoading.show();
        }
    }

    @Override
    public void dismissLoading() {
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
        }
    }

    public void initToolBar(String title) {
        initToolBar(false, title, null);
    }

    public void initToolBar(String title, View.OnClickListener onClickListener) {
        initToolBar(false, title, onClickListener);
    }

    public void initToolBarMenu(Boolean hideBack, String title, Boolean hideMenuIv) {
        initToolBar(hideBack, title, null);
        initToolBarMenu(hideMenuIv, null);
    }

    public void initToolBarMenu(Boolean hideBack, String title, Boolean hideMenuIv, View.OnClickListener onClickListener) {
        initToolBar(hideBack, title, onClickListener);
        initToolBarMenu(hideMenuIv, onClickListener);
    }

    public void initToolbarAction(Boolean hideBack, String title, Boolean hideMenuTv, View.OnClickListener onClickListener) {
        initToolbarAction(hideMenuTv, onClickListener);
        initToolBar(hideBack, title, onClickListener);
    }

    public void initToolbarAction(Boolean hideMenuTv, View.OnClickListener onClickListener) {
        if (toolbar_menu_tv == null) {
            return;
        }
        toolbar_menu_tv.setVisibility(hideMenuTv ? VISIBLE : GONE);
        toolbar_menu_tv.setOnClickListener(onClickListener);
    }

    public void initToolBarMenu(Boolean hideMenuIv, View.OnClickListener onClickListener) {
        if (toolbar_menu == null) {
            return;
        }
        toolbar_menu.setVisibility(hideMenuIv ? VISIBLE : GONE);
        toolbar_menu.setOnClickListener(onClickListener);
    }

    public void initToolBar(Boolean hideBack, String title, View.OnClickListener onClickListener) {
        if (toolbar_back == null || toolbar_title == null) {
            return;
        }
        toolbar_back.setVisibility(hideBack ? VISIBLE : GONE);
        toolbar_title.setText(title);
        toolbar_back.setOnClickListener(onClickListener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mLoading != null && mLoading.isShowing()) {
                mLoading.dismiss();
                return true;
            }else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onResume() {
        super.onResume();
        initBar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBind != null){
            mBind.unbind();
        }
    }

    protected abstract int getLayoutId();
}
