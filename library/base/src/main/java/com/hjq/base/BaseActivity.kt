package com.hjq.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.hjq.base.action.*
import java.util.*
import kotlin.math.pow

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : Activity 技术基类
 */
abstract class BaseActivity : AbsBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity()
    }

    /**
     * 获取布局 ID
     */
    protected abstract fun getLayoutId(): Int


    /**
     * 初始化布局
     */
     override fun initLayout() {
        if (getLayoutId() > 0) {
            setContentView(getLayoutId())
            initSoftKeyboard()
        }
    }






}