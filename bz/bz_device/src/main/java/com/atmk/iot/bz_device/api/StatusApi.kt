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
 class StatusApi( @HttpIgnore private var  stataus: Int, @HttpIgnore private var  productId: String) : IRequestApi , IRequestType ,IRequestHost{

    override fun getHost() = "http://101.200.187.211:8000/"
    override fun getType() = BodyType.JSON
    override fun getApi() : String {
        when (stataus) {
            0 -> {
                return if(productId!="")
                    "jetlinks/device-instance/_count?terms[0].column=productId&terms[0].value=${productId}"

                else
                    "jetlinks/device-instance/_count"
            }
            1 -> {
                return if(productId!="")
                   "jetlinks/device-instance/_count?terms[0].column=state&terms[0].value=online&terms[1].column=productId&terms[1].value=${productId}"
                else
                    "jetlinks/device-instance/_count?terms[0].column=state&terms[0].value=online"
            }
            2 -> {
                return if(productId!="")
                    "jetlinks/device-instance/_count?terms[0].column=state&terms[0].value=offline&terms[1].column=productId&terms[1].value=${productId}"
                else
                    "jetlinks/device-instance/_count?terms[0].column=state&terms[0].value=offline"
            }
            3 -> {
                return if(productId!="")
                    "jetlinks/device-instance/_count?terms[0].column=state&terms[0].value=notActive&terms[1].column=productId&terms[1].value=${productId}"
                else
                    "jetlinks/device-instance/_count?terms[0].column=state&terms[0].value=notActive"
            }
            else -> throw return "jetlinks/device-instance/_count"
        }
    }


 }