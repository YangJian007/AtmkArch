package com.atmk.base.other

import com.alibaba.android.arouter.launcher.ARouter
import com.atmk.base.arouter.BuildConfigService



object AppConfig {


    private val service: BuildConfigService
        get() = ARouter.getInstance().build("/iot/buildConfig").navigation() as BuildConfigService


    /**
     * 当前是否为调试模式
     */
    fun isDebug(): Boolean {
//        return BuildConfig.DEBUG
        return service.isDebug()
    }


    /**
     * 当前是否要开启日志打印功能
     */
    fun isLogEnable(): Boolean {
//        return BuildConfig.LOG_ENABLE
        return service.isLogEnable()
    }


    /**
     * 获取服务器主机地址
     */
    fun getHostUrl(): String {
//        return BuildConfig.HOST_URL
        return service.getHostUrl()
    }
}