package com.atmk.iot.bz_statistics.mvvm.v

import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.atmk.base.app.AppVMActivity
import com.atmk.iot.bz_statistic.databinding.ActivityMvvmBinding
import com.atmk.iot.bz_statistics.mvvm.vm.MVVMViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map

/**
 * @author 杨剑
 * @fileName
 * @date 2022-09-13
 * @describe
 * @changeUser
 * @changTime
 */
@AndroidEntryPoint
class MVVMActivity : AppVMActivity<ActivityMvvmBinding>() {

    //viewModel
    private val viewModel by viewModels<MVVMViewModel>()


    override fun getViewBinding(): ActivityMvvmBinding {
        return ActivityMvvmBinding.inflate(layoutInflater)
    }

    override fun initView() {

        mViewBinding.btn.setOnClickListener {
            loadData()
        }

        lifecycleScope.launchWhenResumed {
            viewModel.userInfo
                .map { it?.username }
                .collect {
                mViewBinding.tv.text = it
            }
        }

        mViewBinding.btn2.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                delay(100)
                throw IndexOutOfBoundsException()
            }
        }

    }

    override fun initData() {
        loadData()
    }

    //获取数据
    private fun loadData() {
        viewModel.getUserInfo("15225937677", "yang641052")
    }


}