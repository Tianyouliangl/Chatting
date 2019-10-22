package com.chatting.china.fragment;

import com.chatting.china.R;
import com.chatting.china.common.fragment.BaseMvpFragment;
import com.chatting.china.contract.DynamicContract;
import com.chatting.china.presenter.main.DynamicPresenter;

/**
 * author : fengzhangwei
 * date : 2019/9/17
 */
public class DynamicFragment extends BaseMvpFragment<DynamicContract.IPresenter> implements DynamicContract.IView {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dynamic;
    }

    @Override
    public Class<? extends DynamicContract.IPresenter> registerPresenter() {
        return DynamicPresenter.class;
    }
}
