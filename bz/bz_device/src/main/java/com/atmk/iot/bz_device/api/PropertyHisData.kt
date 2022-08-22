package com.atmk.iot.bz_device.api

import com.hjq.http.annotation.HttpIgnore
import com.hjq.http.config.IRequestApi

/**
 * @author 杨剑
 * @fileName
 * @date 2022-08-02
 * @describe
 * @changeUser
 * @changTime
 */
class PropertyHisData : IRequestApi {

    override fun getApi(): String {
        return "device-instance/${deviceId}/properties/_query?terms[0].column=timestamp\$BTW&terms[0].value=${startDate} 00:00:00,${endDate} 23:59:59&terms[1].column=property&terms[1].value=${propertyName}&sorts[0].name=timestamp&sorts[0].order=desc&pageIndex=${pageNum}&pageSize=${pageSize}"
    }

    @HttpIgnore
    var deviceId: String = ""

    @HttpIgnore
    var startDate: String = ""

    @HttpIgnore
    var endDate: String = ""

    @HttpIgnore
    var propertyName: String = ""

    @HttpIgnore
    var pageNum: Int = 0

    @HttpIgnore
    var pageSize: Int = 10

    data class Bean(
        val `data`: List<Data>,
        val pageIndex: Int,
        val pageSize: Int,
        val total: Int
    )

    data class Data(
        val createTime: Long,
        val deviceId: String,
        val formatValue: String,
        val id: String,
        val numberValue: Double,
        val `property`: String,
        val propertyName: String,
        val timestamp: Long,
        val type: String,
        val value: Double
    )

}