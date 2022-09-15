package com.atmk.base.app

import androidx.viewbinding.ViewBinding
import com.atmk.base.action.ToastAction
import com.atmk.base.http.model.HttpData
import com.hjq.base.AbsBaseActivity
import com.hjq.base.BaseVMFragment
import com.hjq.http.listener.OnHttpListener
import okhttp3.Call

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : Fragment 业务基类
 */
abstract class AppVMFragment<T : ViewBinding,A : AbsBaseActivity> : BaseVMFragment<T,A>(),
    ToastAction, OnHttpListener<Any> {

    /**
     * 当前加载对话框是否在显示中
     */
    open fun isShowDialog(): Boolean {
        val activity: A = getAttachActivity() ?: return false
        return activity.isShowDialog()
    }

    /**
     * 显示加载对话框
     */
    open fun showDialog() {
        getAttachActivity()?.showDialog()
    }

    /**
     * 隐藏加载对话框
     */
    open fun hideDialog() {
        getAttachActivity()?.hideDialog()
    }

    /**
     * [OnHttpListener]
     */
    override fun onStart(call: Call) {
        showDialog()
    }

    override fun onSucceed(result: Any) {
        if (result !is HttpData<*>) {
            return
        }
        toast(result.getMessage())
    }

    override fun onFail(e: Exception) {
        toast(e.message)
    }

    override fun onEnd(call: Call) {
        hideDialog()
    }
}