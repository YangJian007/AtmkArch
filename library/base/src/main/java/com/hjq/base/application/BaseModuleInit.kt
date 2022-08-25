package com.hjq.base.application

/**
 * @author 杨剑
 * @fileName
 * @date 2022-08-24
 * @describe Module实现类
 * @changeUser
 * @changTime
 */
abstract class BaseModuleInit: IModuleInit {
    /**
     * 模块初始化优先级 越高初始化越快
     * Module中设置优先级越小越优先初始化
     */
    open val priority: Int = 0
}