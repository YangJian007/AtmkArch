package com.atmk.iot.bz_device.ui.fragment

import com.atmk.base.app.AppActivity
import com.atmk.base.app.TitleBarFragment
import com.atmk.iot.bz_device.R

/**
 *    设备详情-日志管理Fm
 */
class LogManagerFragment : TitleBarFragment<AppActivity>() {

    companion object {

        fun newInstance(): LogManagerFragment {
            return LogManagerFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.log_manager_fragment
    }

    override fun initView() {}

    override fun initData() {}
}