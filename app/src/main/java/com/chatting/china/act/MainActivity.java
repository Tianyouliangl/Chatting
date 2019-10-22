package com.chatting.china.act;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;

import com.chatting.china.ChattingApplication;
import com.chatting.china.R;
import com.chatting.china.common.act.BaseMvpFragmentActivity;
import com.chatting.china.contract.MainContract;
import com.chatting.china.fragment.ContactFragment;
import com.chatting.china.fragment.DialogueFragment;
import com.chatting.china.fragment.DynamicFragment;
import com.chatting.china.presenter.main.MainPresenter;
import com.chatting.china.widgets.SlidingMenu;
import com.chatting.china.widgets.TabLayout;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.BindView;
import me.leolin.shortcutbadger.ShortcutBadger;


public class MainActivity extends BaseMvpFragmentActivity<MainContract.IPresenter> implements MainContract.IView, TabLayout.OnTabClickListener {

    @BindView(R.id.mTabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.sliding_menu)
    SlidingMenu sliding_menu;

    private Fragment mCurrentFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public Class<? extends MainContract.IPresenter> registerPresenter() {
        return MainPresenter.class;
    }

    @Override
    public void initView() {
        super.initView();
        ArrayList<TabLayout.Tab> tabs = new ArrayList<>(3);
        tabs.add(new TabLayout.Tab(R.drawable.selector_dialogue, R.string.dialogue, DialogueFragment.class));
        tabs.add(new TabLayout.Tab(R.drawable.selector_contact, R.string.contact, ContactFragment.class));
        tabs.add(new TabLayout.Tab(R.drawable.selector_dynamic, R.string.dynamic, DynamicFragment.class));
        mTabLayout.setUpData(tabs, this);
        mTabLayout.setCurrentTab(0);
    }

    @Override
    public void onTabClick(TabLayout.Tab tab) {
        try {
            Fragment tmpFragment = getSupportFragmentManager().findFragmentByTag(tab.targetFragmentClz.getSimpleName());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (tmpFragment == null) {
                tmpFragment = tab.targetFragmentClz.newInstance();
                transaction.add(R.id.mFragmentContainerLayout, tmpFragment, tab.targetFragmentClz.getSimpleName());
                if (mCurrentFragment != null) {
                    transaction.hide(mCurrentFragment);
                }
                transaction.commit();
            } else {
                transaction.show(tmpFragment);
                if (mCurrentFragment != null) {
                    transaction.hide(mCurrentFragment);
                }
                transaction.commit();
            }
            mCurrentFragment = tmpFragment;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (sliding_menu != null){
                if (sliding_menu.getMenuStatus()){
                    sliding_menu.closeMenu();
                    return true;
                }else {
                    moveTaskToBack(false);
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
