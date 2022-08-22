package com.atmk.iot.bz_device.api

import com.hjq.http.config.IRequestApi
import com.hjq.http.config.IRequestHost

/**
 * @author 杨剑
 * @fileName
 * @date 2022-07-27
 * @describe
 * @changeUser
 * @changTime
 */
data class VideoUrlApi(val stcd: String) : IRequestHost, IRequestApi {
    override fun getHost() = "http://101.200.187.211:8000/"

    override fun getApi() = "video/v1/play/video_play_stcd"

    data class Bean(
        val flvUrl: String,
        val guid: Int,
        val message: String,
        val modeName: String,
        val mopoType: Int,
        val playUrl: String,
        val rate: String,
        val rtspUrl: String,
        val ssrc: String,
        val status: Int,
        val stream: String
    )

}