package com.atmk.iot.bz_login.api

import com.hjq.http.config.IRequestApi
import java.net.URLEncoder

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2019/12/07
 *    desc   : 用户登录
 */
class VersionCodeApi : IRequestApi {

    override fun getApi()= "authorize/captcha/image"


    data class Bean(
        val key: String,
        val base64: String
    )
}