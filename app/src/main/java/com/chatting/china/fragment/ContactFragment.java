package com.chatting.china.fragment;

import com.chatting.china.R;
import com.chatting.china.common.act.BaseMvpActivity;
import com.chatting.china.common.fragment.BaseMvpFragment;
import com.chatting.china.contract.ContactContract;
import com.chatting.china.presenter.main.ContactPresenter;

/**
 * author : fengzhangwei
 * date : 2019/9/17
 */
public class ContactFragment extends BaseMvpFragment<ContactContract.IPresenter> implements ContactContract.IView {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contact;
    }

    @Override
    public Class<? extends ContactContract.IPresenter> registerPresenter() {
        return ContactPresenter.class;
    }

}
