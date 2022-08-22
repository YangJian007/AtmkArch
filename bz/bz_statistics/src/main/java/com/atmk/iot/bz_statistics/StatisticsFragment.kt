package com.atmk.iot.bz_statistics

import com.alibaba.android.arouter.facade.annotation.Route
import com.atmk.base.app.AppActivity
import com.atmk.base.app.TitleBarFragment
import com.atmk.iot.bz_statistic.R

/**
 * @author 杨剑
 * @fileName
 * @date 2022-08-22
 * @describe
 * @changeUser
 * @changTime
 */
@Route(path = "/statistics/statisticsFm")
class StatisticsFragment :TitleBarFragment<AppActivity>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_statictics
    }

    override fun initView() {

    }

    override fun initData() {

    }
}