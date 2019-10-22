package com.chatting.china.common;

/**
 * 常量池
 * Author:Ljb
 * Time:2018/12/28
 * There is a lot of misery in life
 */
public interface Constant {

    boolean IS_DEBUG = true;

    //TODO bugly 请更换项目的appId
    String APP_ID_BUGLY = "96e26c58e7";
    //科大讯飞appId
    String APP_ID_SPEECH = "5caaf9d6";

    int PAGE_SIZE = 20;

    String HOST_HTTP = IS_DEBUG ? Test.HOST_HTTP : Release.HOST_HTTP;
    String HOST_SOCKET = IS_DEBUG ? Test.HOST_SOCKET : Release.HOST_SOCKET;
    String HOST_SOCKET_HISTORY = IS_DEBUG ? Test.HOST_CHAT_HISTORY : Release.HOST_CHAT_HISTORY;
    String H5_HOST = IS_DEBUG ? Test.H5_HOST : Release.H5_HOST;
    String BaseUrl = "http://172.16.200.235:8081/";
    String USER_PHONE = "user_phone";
    String USER_PASSWORD = "user_pwd";

    //测试环境
    interface Test {
        //todo 修改为实际项目地址
        String HOST_HTTP = "http://xiaohuatuo-api.lugangtech.com";
        String HOST_SOCKET = "https://testxhttcp.cinyi.com";
        String H5_HOST = "http://xiaohuatuo-doctor-h5.lugangtech.com";
        String API_CHAT_HISTORY_TEST = "https://testxhtapi.cinyi.com/";
        String HOST_CHAT_HISTORY = API_CHAT_HISTORY_TEST + "api/message/history";
    }

    //线上环境
    interface Release {
        //todo 修改为实际项目地址
        String HOST_HTTP = "";
        String HOST_SOCKET = "";
        String H5_HOST = "";
        String API_CHAT_HISTORY_TEST = "https://testxhtapi.cinyi.com";
        String HOST_CHAT_HISTORY = API_CHAT_HISTORY_TEST + "api/message/history";
    }

    //RequestCode
    interface ReqCode {
        int CODE_PIC_LIB = 0x001;
        int CODE_TAKE_PIC = 0x002;
    }

    //权限Code
    interface PermissionCode {
        int CODE_INIT = 0x00;
        int CODE_IM = 0x002;
        int CODE_TAKE_PIC = 0x003;
        int CODE_AUDIO = 0x004;
        int CODE_PIC_LIB = 0x005;
    }

    //SPKey
    interface SPKey {
        String KEY_UID = "uid";
        String KEY_TOKEN = "token";
        String KEY_NAME = "name";
        String KEY_IMG = "headImg";
        String USER_TYPE = "userType";
        String SWITCH_NOTIFICATION = "sw_notify";
        String KEY_USER_INFO = "userInfo";
        String WELCOME = "WELCOME";
    }

    //数据库
    interface DB {
        String NAME = "db_senyint";
        int VERSION = 1;
    }

    //H5页面
    interface H5 {
        // 设置
        String SETUP = H5_HOST + "/setup.html";

        // 患者详情页
        String PATIENT_DETAIL = H5_HOST + "/patient-detail.html?id=";

        // 我的账户
        String MY_ACCOUNT = H5_HOST + "/account.html";

        // 个人资料详情页
        String MY_INFO = H5_HOST + "/myinfo.html";

        // 金豆商城
        String VIRTUAL_MALL = H5_HOST + "/virtual-mall.html";

        // 我的绑定码
        String MY_CODE = H5_HOST + "/myqrcode.html";

        // 我的订单
        String MY_ORDER = H5_HOST + "/myorder.html";

        // 患者评估
        String ESTIMATE = H5_HOST + "/estimate.html";
    }

}
