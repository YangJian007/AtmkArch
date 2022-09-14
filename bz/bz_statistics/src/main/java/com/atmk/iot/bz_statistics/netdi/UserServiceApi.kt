package com.atmk.iot.bz_statistics.netdi

import com.atmk.iot.bz_statistics.mvvm.UserInfo
import com.yang.hilt.net.BaseReq
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author 杨剑
 * @fileName
 * @date 2022-09-14
 * @describe
 * @changeUser
 * @changTime
 */
interface UserServiceApi {

    @POST("user/login")
    @FormUrlEncoded
    suspend fun login1(
        @Field("username") username: String,
        @Field("password") password: String
    ): BaseReq<UserInfo?>


}