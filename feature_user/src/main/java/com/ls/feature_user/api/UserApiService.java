package com.ls.feature_user.api;


import com.ls.feature_user.bean.ReqMobileLogin;
import com.ls.feature_user.bean.ReqResetPwd;
import com.ls.feature_user.bean.ReqSendSmsCode;
import com.ls.feature_user.bean.ReqUpdateUserProfile;
import com.ls.feature_user.bean.ResLogin;
import com.ls.feature_user.bean.ResUpload;
import com.ls.libbase.bean.ResUser;
import com.ls.network.bean.ResBase;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * 这里存放user模块的api
 */
public interface UserApiService {

    /**
     * 使用手机号发送验证码
     * @param sendSmsCode 请求体
     * @return
     */
    @POST("addons/cms/api.sms/send")
    Call<ResBase<ResBase>> sendSmsCode(@Body ReqSendSmsCode sendSmsCode);


    /**
     * 使用手机号和验证码登录
     * @param login 请求体
     * @return
     */
    @POST("addons/cms/api.login/mobilelogin")
    Call<ResBase<ResLogin>> mobileLogin(@Body ReqMobileLogin login);


    /**
     *获取用户信息
     * @param userId
     * @param type
     * @return
     */
    @GET("addons/cms/api.user/userInfo")
    Call<ResBase<ResUser>> getUserInfo(@Query("user_id") String userId,@Query("type") String type);


    /**
     * 重置密码
     *
     * @param code 请求体
     * @return
     */
    @POST("addons/cms/api.login/resetpwd")
    Call<ResBase<ResBase>> resetPassword(@Header("token") String token, @Body ReqResetPwd code);


    /**
     * 向服务器发出退出登录请求
     *
     *
     * @return
     */
    @POST("addons/cms/api.user/logout")
    Call<ResBase<ResBase>> logout(@Header("token") String token);


    /**
     * 修改用户信息
     *
     * @return
     */
    @POST("addons/cms/api.user/profile")
    Call<ResBase<ResUser>> updateUserProfile(@Header("token") String token, @Body ReqUpdateUserProfile profile);

    @Multipart //标识这个请求是一个multipart/form-data 表单提交请求
    @POST("api/common/upload")
    Call<ResBase<ResUpload>> uploadFile(@Header("token") String token, @Part MultipartBody.Part file);

}
