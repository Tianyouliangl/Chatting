package com.chatting.china.protocol;



import com.chatting.china.domain.BaseResponse;
import com.chatting.china.domain.LoginBean;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * author : fengzhangwei
 * date : 2019/9/11
 */
public interface IHttpProtocol {
    @POST("login")
    @FormUrlEncoded
    Observable<BaseResponse<LoginBean>> login(@FieldMap HashMap<String, String> map);
}
