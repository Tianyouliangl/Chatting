package com.chatting.china.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.chatting.china.R;


/**
 * author : fengzhangwei
 * date : 2019/9/12
 */
public class LoadingDialog extends Dialog {


    private  Boolean mIsBack;

    public LoadingDialog(@NonNull Context context, Boolean isBack) {
        super(context);
        this.mIsBack = isBack;
        setContentView(View.inflate(getContext(), R.layout.dialog_loading, null));
        setCancelable(mIsBack);
        setCanceledOnTouchOutside(isBack);
    }
}
