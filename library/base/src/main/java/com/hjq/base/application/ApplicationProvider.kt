package com.hjq.base.application

import android.app.Application
import android.content.Context

/**
 * @author 杨剑
 * @fileName
 * @date 2022-08-24
 * @describe 让ApplicationProvider继承自Application
 * @changeUser
 * @changTime
 */

open class ApplicationProvider : Application() {

    companion object {
        // 全局共享的 Application
        lateinit var appContext: Application
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        ModuleInitDelegate.reorder()
        ModuleInitDelegate.onCreate()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        ModuleInitDelegate.attachBaseContext(base)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        ModuleInitDelegate.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        ModuleInitDelegate.onTrimMemory(level)
    }

    override fun onTerminate() {
        super.onTerminate()
        ModuleInitDelegate.onTerminate()
    }
}
