package com.atmk.iot.bz_device.api

import com.hjq.http.annotation.HttpIgnore
import com.hjq.http.config.IRequestApi
import com.hjq.http.config.IRequestHost
import com.hjq.http.config.IRequestType
import com.hjq.http.model.BodyType

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2019/12/07
 *    desc   : 可进行拷贝的副本
 */
class DeleteApi(@HttpIgnore private var id: String?) : IRequestApi,IRequestHost ,IRequestType{

    override fun getApi(): String {
        return  "jetlinks/device-instance/${id}"
        return "jetlinks/device-instance/${id}/undeploy"
    }

    override fun getHost(): String {
        return "http://101.200.187.211:8000/"
    }

    override fun getType(): BodyType {
        return  BodyType.JSON
    }

}