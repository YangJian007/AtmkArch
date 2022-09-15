package com.atmk.iot

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

/**
 * @author 杨剑
 * @fileName
 * @date 2022-09-11
 * @describe  kotlin协程全局异常捕获器
 * @changeUser
 * @changTime
 */
class GlobalCoroutineExceptionHandler:CoroutineExceptionHandler {
    override val key=CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        exception.printStackTrace()
    }


}