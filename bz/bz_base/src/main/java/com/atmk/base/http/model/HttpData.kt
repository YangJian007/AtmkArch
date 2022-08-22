package com.atmk.base.http.model

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2019/12/07
 *    desc   : 统一接口数据结构
 */
open class HttpData<T> {

    /** 返回码 */
    private val status: Int = 0

    /** 提示语 */
    private val message: String? = null

    /** 数据 */
    private val result: T? = null

    private val data: T? = null

    fun getCode(): Int {
        return status
    }

    fun getMessage(): String? {
        return message
    }

    fun getData(): T? {
        return result ?: data
    }

    /**
     * 是否请求成功
     */
    fun isRequestSucceed(): Boolean {
        return status == 200 || status == 1
    }

    /**
     * 是否 Token 失效
     */
    fun isTokenFailure(): Boolean {
        return status == 401
    }
}