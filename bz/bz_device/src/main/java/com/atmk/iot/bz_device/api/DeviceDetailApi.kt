package com.atmk.iot.bz_device.api

import com.hjq.http.annotation.HttpIgnore
import com.hjq.http.config.IRequestApi

/**
 * @author 杨剑
 * @fileName
 * @date 2022-07-21
 * @describe
 * @changeUser
 * @changTime
 */
class DeviceDetailApi(@HttpIgnore var deviceID: String) : IRequestApi {

    override fun getApi() = "device/instance/${deviceID}/detail"

    data class Bean(
        val address: String,
        val aloneConfiguration: Boolean,
        val binds: List<Any>,
        val cachedConfiguration: CachedConfiguration,
        val configuration: Configuration,
        val connectServerId: String,
        val createTime: Long,
        val deviceType: DeviceType,
        val features: List<Any>,
        val id: String,
        val independentMetadata: Boolean,
        val metadata: String,
        val name: String,
        val offlineTime: Long,
        val onlineTime: Long,
        val productId: String,
        val productName: String,
        val protocol: String,
        val protocolName: String,
        val registerTime: Long,
        val state: State,
        val tags: List<Any>,
        val transport: String
    )

    class CachedConfiguration

    data class Configuration(
        val deviceName: String,
        val productName: String
    )

    data class DeviceType(
        val text: String,
        val value: String
    )

    data class State(
        val text: String,
        val value: String
    )

    //元数据
    data class MetaData(

        val functions: List<Function>,

        )

    data class Function(
        val async: Boolean,
        val id: String,
        val inputs: List<Input>,
        val name: String,
        val output: Output
    )


    data class Input(
        val description: String,
        val id: String,
        val name: String,
        val valueType: ValueType
    )

    data class Output(
        val expands: Expands,
        val type: String
    )

    data class ValueType(
        val elementType: ElementType,
        val elements: List<Element>,
        val expands: Expands,
        val type: String,
        val unit: String
    )

    data class ElementType(
        val scale: Int,
        val type: String
    )

    data class Element(
        val id: Int,
        val text: String,
        val value: String
    )

    data class Expands(
        val maxLength: String
    )


}