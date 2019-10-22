package com.chatting.china.fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;

import com.chatting.china.R;
import com.chatting.china.common.fragment.BaseMvpFragment;
import com.chatting.china.contract.DialogueContract;
import com.chatting.china.presenter.main.DialoguePresenter;

import butterknife.BindView;
import me.grantland.widget.AutofitTextView;

/**
 * author : fengzhangwei
 * date : 2019/9/17
 */
public class DialogueFragment extends BaseMvpFragment<DialogueContract.IPresenter> implements DialogueContract.IView {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dialogue;
    }

    @Override
    public Class<? extends DialogueContract.IPresenter> registerPresenter() {
        return DialoguePresenter.class;
    }

    @Override
    public void initData() {
        super.initData();

    }
}
