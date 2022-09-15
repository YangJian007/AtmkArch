package com.atmk.iot.bz_statistics

import android.content.Intent
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.atmk.base.app.AppActivity
import com.atmk.base.app.TitleBarFragment
import com.atmk.base.app.TitleBarVMFragment
import com.atmk.iot.bz_statistic.R
import com.atmk.iot.bz_statistic.databinding.FragmentStaticticsBinding
import com.atmk.iot.bz_statistics.mvvm.v.MVVMActivity
import com.hjq.base.AbsBaseActivity

/**
 * @author 杨剑
 * @fileName
 * @date 2022-08-22
 * @describe
 * @changeUser
 * @changTime
 */
@Route(path = "/statistics/statisticsFm")
class StatisticsFragment : TitleBarVMFragment<FragmentStaticticsBinding,AbsBaseActivity>() {

    @JvmField
    @Autowired
    var aaa: String? = null


    override fun getViewBinding(inflater: LayoutInflater): FragmentStaticticsBinding {
        return FragmentStaticticsBinding.inflate(inflater)
    }

    override fun initView() {
        ARouter.getInstance().inject(this)

        mViewBinding?.let {
            it.btn.setOnClickListener {
                startActivity(Intent(requireActivity(), MVVMActivity::class.java))
            }
        }
    }

    override fun initData() {
        toast(aaa)
    }



}