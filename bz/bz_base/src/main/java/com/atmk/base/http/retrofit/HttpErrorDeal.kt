/*
 *
 *  * Copyright (C)  HuangLinqing, TravelPrevention Open Source Project
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.atmk.base.http.retrofit


import com.hjq.toast.ToastUtils
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException

/**
 * @author：HuangLinqing
 * @blog：https://huanglinqing.blog.csdn.net/?t=1
 * @公众号：Android 技术圈
 * @desc：统一处理异常
 */
object HttpErrorDeal {

    /**
     * 处理 http异常
     * @param error 异常信息
     * @param deal 异常时处理方法
     */
    fun dealHttpError(error: Throwable, deal: (() -> Unit)? = null) {
        when (error) {
            is SocketException -> {
                ToastUtils.show("服务器连接异常")
            }
            is HttpException -> {
                ToastUtils.show("服务器连接失败")
            }
            is SocketTimeoutException -> {
                ToastUtils.show("请求超时，请检查网络连接")
            }
            is IOException -> {
                ToastUtils.show("服务器连接失败")
            }
            is CancellationException -> {
                //协程被取消 这里是正常的 不提示
            }
            else -> {
                error.message?.let {
                    if (it.isNotEmpty()) {
                        ToastUtils.show(it)
                    } else {
                        ToastUtils.show("空指针异常")
                    }
                }
            }
        }

        if (deal != null) {
            deal()
        }
    }
}