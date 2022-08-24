package com.atmk.base.arouter

import com.alibaba.android.arouter.facade.template.IProvider

/**
 * @author 杨剑
 * @fileName
 * @date 2022-08-24
 * @describe
 * @changeUser
 * @changTime
 */
interface BuildConfigService : IProvider {

    /**
     * 当前是否为调试模式
     */
    fun isDebug(): Boolean


    /**
     * 当前是否要开启日志打印功能
     */
    fun isLogEnable(): Boolean


    /**
     * 获取服务器主机地址
     */
    fun getHostUrl(): String

}