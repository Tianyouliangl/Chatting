package com.chatting.china.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * author : fengzhangwei
 * date : 2019/9/20
 */
public class MyTextView extends TextView {


    public MyTextView(Context context) {
        this(context,null);
    }

    public MyTextView(Context context,  @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
