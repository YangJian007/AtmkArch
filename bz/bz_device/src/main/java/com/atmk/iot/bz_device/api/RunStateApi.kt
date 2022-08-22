package com.atmk.iot.bz_device.api

import com.hjq.http.config.IRequestApi
import com.hjq.http.config.IRequestType
import com.hjq.http.model.BodyType

/**
 * @author 杨剑
 * @fileName
 * @date 2022-07-26
 * @describe
 * @changeUser
 * @changTime
 */
data class RunStateApi(
    val dashboard: String="device",
    val dimension: String="history",
    val measurement: String="properties",
    val `object`: String,
    val params: Params
) : IRequestApi, IRequestType {
    override fun getApi()="dashboard/_multi"

    override fun getType()= BodyType.JSON


    data class Params(
        val deviceId: String
    )

    class Bean : ArrayList<BeanItem>()

    data class BeanItem(
        val `data`: Data
    )

    data class Data(
        val timeString: String,
        val timestamp: Long,
        val value: Value
    )

    data class Value(
        val createTime: Long,
        val deviceId: String,
        val formatValue: String,
        val id: String,
        val `property`: String,
        val propertyName: String,
        val timestamp: Long,
        val type: String,
        val value: String
    )


}


