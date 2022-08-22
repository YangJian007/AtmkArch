package com.atmk.iot.bz_device.api

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
class AddDeviceApi : IRequestApi,IRequestHost ,IRequestType{

    override fun getApi(): String {
        return "jetlinks/device-instance"
    }

    override fun getHost(): String {
        return "http://101.200.187.211:8000/"
    }

    override fun getType(): BodyType {
        return  BodyType.JSON
    }
    var state: List<State>?=null
    var id: String?=null
    var  name: String?=null
    var  productName: String?=null
    var  productId:String?=null
    var  describe: String?=null
    data class Data(
            val id: String,
            val name: String,
            val describe: String,
            val productId: String,
            val productName: String,
            val configuration: Configuration,
            val createTime: Long,
            val creatorId: String,
            val creatorName: String,
            val features: List<Any>,
            val modifierId: String,
            val modifierName: String,
            val modifyTime: Long,
            val state: State,
    )

    data class Configuration(
            val deviceName: String,
            val productName: String
    )

    data class State(
            var text: String,
            var value: String
    )

    data class ResultBean(
            var added: String,
            var updated: String,
            var total: String
    )
}