package com.atmk.iot.bz_device.api

import com.hjq.http.annotation.HttpIgnore
import com.hjq.http.config.IRequestApi

/**
 *    desc   : 发送控制指令
 */
class SendCMDApi(
    @HttpIgnore private var deviceId: String,
    @HttpIgnore private var functionId: String
) : IRequestApi {

    override fun getApi() = "device/invoked/${deviceId}/function/${functionId}"

    class Bean : ArrayList<String>()
}