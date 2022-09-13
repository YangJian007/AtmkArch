package com.atmk.iot.bz_statistics.mvvm

import androidx.activity.viewModels
import com.atmk.base.app.AppVMActivity
import com.atmk.iot.bz_statistic.databinding.ActivityMvvmBinding

/**
 * @author 杨剑
 * @fileName
 * @date 2022-09-13
 * @describe
 * @changeUser
 * @changTime
 */
class MVVMActivity : AppVMActivity<ActivityMvvmBinding>() {

    //viewModel
    private val viewModel by viewModels<MVVMViewModel>()


    override fun getViewBinding(): ActivityMvvmBinding {
        return ActivityMvvmBinding.inflate(layoutInflater)
    }

    override fun initView() {

        mViewBinding.btn.setOnClickListener {
            viewModel.getUserInfo("15225937677","yang641052")
                .observe(this){
                    mViewBinding.tv.text=it.toString()
                }
        }

    }

    override fun initData() {

    }


}