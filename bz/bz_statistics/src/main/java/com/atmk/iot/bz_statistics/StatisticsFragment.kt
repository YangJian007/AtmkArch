package com.atmk.iot.bz_statistics

import android.content.Intent
import android.widget.Button
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.atmk.base.app.AppActivity
import com.atmk.base.app.TitleBarFragment
import com.atmk.iot.bz_statistic.R
import com.atmk.iot.bz_statistics.mvvm.v.MVVMActivity

/**
 * @author 杨剑
 * @fileName
 * @date 2022-08-22
 * @describe
 * @changeUser
 * @changTime
 */
@Route(path = "/statistics/statisticsFm")
class StatisticsFragment : TitleBarFragment<AppActivity>() {

    @JvmField
    @Autowired
    var aaa: String? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_statictics
    }



    override fun initView() {
        ARouter.getInstance().inject(this)

        findViewById<Button>(R.id.btn)?.setOnClickListener {
            startActivity(Intent(requireActivity(), MVVMActivity::class.java))
        }
    }

    override fun initData() {
        toast(aaa)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}