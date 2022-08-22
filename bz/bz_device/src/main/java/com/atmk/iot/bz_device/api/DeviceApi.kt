package com.atmk.iot.bz_device.api

import com.hjq.http.annotation.HttpIgnore
import com.hjq.http.config.IRequestApi
import com.hjq.http.config.IRequestType
import com.hjq.http.model.BodyType

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2019/12/07
 *    desc   : 可进行拷贝的副本
 */
class DeviceApi(
    @HttpIgnore private var index: Int, @HttpIgnore private var productId: String,
    @HttpIgnore private var name: String,@HttpIgnore private var deviceStatus: String
) : IRequestApi, IRequestType {
    override fun getApi(): String {
        return if (productId != "" && name != ""  &&deviceStatus!="") {
         var old=  "device-instance/_query?pageIndex=${index}&pageSize=10&terms[0].column=productId&terms[0].value=${productId}&terms[1].column=state&terms[1].value=${deviceStatus}&terms[2].column=name\$like&terms[2].value=%${name}%"
            old=   old.replace("%","%25")
            old=  old.replace("[","%5B")
            old=   old.replace("]","%5D")
            return old
        }
        else if (productId != "" && name != "" ){
            var old=  "device-instance/_query?pageIndex=${index}&pageSize=10&terms[0].column=productId&terms[0].value=${productId}&terms[1].column=name\$like&terms[1].value=%${name}%"
            old=   old.replace("%","%25")
            old=  old.replace("[","%5B")
            old=   old.replace("]","%5D")
            return old
        }else if (productId != "" && deviceStatus != "" ){
            var old=  "device-instance/_query?pageIndex=${index}&pageSize=10&terms[0].column=productId&terms[0].value=${productId}&terms[1].column=state&terms[1].value=${deviceStatus}"
            old=  old.replace("[","%5B")
            old=   old.replace("]","%5D")
            return old
        } else if (name != "" && deviceStatus != "" ){
            var old=  "device-instance/_query?pageIndex=${index}&pageSize=10&terms[0].column=state&terms[0].value=${deviceStatus}&terms[1].column=name\$like&terms[1].value=%${name}%"
             old=   old.replace("%","%25")
             old=  old.replace("[","%5B")
             old=   old.replace("]","%5D")
            return old
        }
        else if (productId != "")
            "device-instance/_query?pageIndex=${index}&pageSize=10&terms[0].column=productId&terms[0].value=${productId}"
        else if (name != "")
            "device-instance/_query?pageIndex=${index}&pageSize=10&terms[0].column=name\$like&terms[0].value=%25${name}%25"
        else if (deviceStatus != "")
            "device-instance/_query?pageIndex=${index}&pageSize=10&terms[0].column=state&terms[0].value=${deviceStatus}"
        else
            "device-instance/_query?pageIndex=${index}&pageSize=10"
    }

    override fun getType() = BodyType.JSON

    data class Beans(
        val `data`: List<Data>,
        val pageIndex: Int,
        val pageSize: Int,
        val total: Int
    )

    data class Data(
        val configuration: Configuration,
        val createTime: Long,
        val creatorId: String,
        val creatorName: String,
        val features: List<Any>,
        val id: String,
        val modifierId: String,
        val modifierName: String,
        val modifyTime: Long,
        val name: String,
        val productId: String,
        val productName: String,
        val registryTime: Long,
        val state: State,
        val statusUpdateTime: Long,
        val describe: String
    )

    data class Configuration(
        val deviceName: String,
        val productName: String
    )

    data class State(
        val text: String,
        val value: String
    )
}