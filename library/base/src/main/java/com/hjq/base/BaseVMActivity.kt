package com.hjq.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : Activity 技术基类
 */
abstract class BaseVMActivity<T : ViewBinding> : AbsBaseActivity() {


    lateinit var mViewBinding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewBinding = getViewBinding()
        setContentView(mViewBinding.root)

        initActivity()
    }

    abstract fun getViewBinding(): T


    /**
     * 初始化布局
     */
    override fun initLayout() {
        initSoftKeyboard()
    }


}