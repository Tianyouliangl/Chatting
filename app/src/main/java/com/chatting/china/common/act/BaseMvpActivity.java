package com.chatting.china.common.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chatting.china.R;
import com.chatting.china.widgets.BaseNetLayout;
import com.chatting.china.widgets.dialog.LoadingDialog;
import com.chatting.mvplibrary.Contract.IPresenterContract;
import com.chatting.mvplibrary.view.MvpActivity;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * author : fengzhangwei
 * date : 2019/9/11
 */
public abstract class BaseMvpActivity<P extends IPresenterContract> extends MvpActivity<P> implements NetChangeListener {

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
    @BindView(R.id.base_net)
    @Nullable
    BaseNetLayout net_view;
    private LoadingDialog mLoading;
    private Unbinder mBind;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
                .statusBarAlpha(0.3f)  //状态栏透明度，不写默认 0.0f
                .barAlpha(0.3f)  //状态栏和导航栏透明度，不写默认 0.0f
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR) // 隐藏导航栏或者状态栏
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
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
    public void onBackPressed() {
        super.onBackPressed();
        dismissLoading();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBind != null){
            mBind.unbind();
        }
        ImmersionBar.with(this).destroy();
    }

    protected abstract int getLayoutId();
}
