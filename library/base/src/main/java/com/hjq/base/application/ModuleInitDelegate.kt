package com.hjq.base.application

import android.content.Context

/**
 * @author 杨剑
 * @fileName
 * @date 2022-08-24
 * @describe Module代理类
 * @changeUser
 * @changTime
 */
object ModuleInitDelegate : IModuleInit {

    private val moduleList = mutableListOf<IModuleInit>()

    fun register(vararg modules: IModuleInit) {
        moduleList.addAll(modules)
    }

    fun reorder() {
        moduleList.sortBy { (it as BaseModuleInit).priority }
    }

    override fun onCreate() {
        moduleList.forEach { it.onCreate() }
    }

    override fun attachBaseContext(base: Context) {
        moduleList.forEach { it.attachBaseContext(base) }
    }

    override fun onLowMemory() {
        moduleList.forEach { it.onLowMemory() }
    }

    override fun onTrimMemory(level: Int) {
        moduleList.forEach { it.onTrimMemory(level) }
    }

    override fun onTerminate() {
        moduleList.forEach { it.onTerminate() }
    }
}