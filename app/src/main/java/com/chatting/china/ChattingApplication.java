package com.chatting.china;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.chatting.china.common.Constant;
import com.chatting.china.common.act.NetConfig;
import com.orhanobut.logger.Logger;

import net.ljb.kt.HttpConfig;

import java.util.HashMap;

/**
 * author : fengzhangwei
 * date : 2019/9/11
 */
public class ChattingApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext =  this;
        HttpConfig.INSTANCE.init(Constant.BaseUrl,getHeader(),getParams(),true);
        NetConfig.init(this);
        Utils.init(this);
        Logger.init("Log");
    }

    /**
     * 公共Header
     * @return
     */
    private HashMap<String,String> getHeader(){
        HashMap<String, String> hashMap = new HashMap<>();
        return hashMap;
    }

    public static Context getContext(){
        return mContext;
    }

    /**
     * 公共参数
     * @return
     */
    private HashMap<String,String> getParams(){
        HashMap<String, String> hashMap = new HashMap<>();
        return hashMap;
    }
}
