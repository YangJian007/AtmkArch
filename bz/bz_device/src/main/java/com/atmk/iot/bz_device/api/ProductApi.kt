package com.atmk.iot.bz_device.api

import com.hjq.http.config.IRequestApi

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2019/12/07
 *    desc   : 可进行拷贝的副本
 */
class ProductApi : IRequestApi {

    override fun getApi(): String {
        return "device-product/_query/no-paging?paging=false"
    }
    data class ResultBean(
            val id: String,
            val name: String,
    )

}