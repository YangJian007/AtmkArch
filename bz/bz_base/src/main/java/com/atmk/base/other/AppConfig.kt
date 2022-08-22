package com.atmk.base.other


/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2019/09/02
 *    desc   : App 配置管理类
 */
object AppConfig {

    /**
     * 当前是否为调试模式
     */
    fun isDebug(): Boolean {
//        return BuildConfig.DEBUG
        return true
    }


    /**
     * 当前是否要开启日志打印功能
     */
    fun isLogEnable(): Boolean {
//        return BuildConfig.LOG_ENABLE
        return true
    }



    /**
     * 获取服务器主机地址
     */
    fun getHostUrl(): String {
//        return BuildConfig.HOST_URL
        return "http://101.200.187.211:8844/"
    }
}