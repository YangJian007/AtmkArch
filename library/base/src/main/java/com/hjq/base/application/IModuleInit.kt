package com.hjq.base.application

import android.content.Context

/**
 * @author 杨剑
 * @fileName
 * @date 2022-08-24
 * @describe Application接口，供Module实现
 * @changeUser
 * @changTime
 */
interface IModuleInit {

    fun onCreate() {}
    fun attachBaseContext(base: Context) {}
    fun onLowMemory() {}
    fun onTrimMemory(level: Int) {}
    fun onTerminate() {}

}