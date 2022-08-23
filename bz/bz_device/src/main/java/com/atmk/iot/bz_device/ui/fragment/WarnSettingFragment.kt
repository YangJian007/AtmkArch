package com.atmk.iot.bz_device.ui.fragment

import com.atmk.base.app.AppActivity
import com.atmk.base.app.TitleBarFragment
import com.atmk.iot.bz_device.R

/**
 *    设备详情-报警设置Fm
 */
class WarnSettingFragment : TitleBarFragment<AppActivity>() {

    companion object {

        fun newInstance(): WarnSettingFragment {
            return WarnSettingFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.device_warn_setting_fragment
    }

    override fun initView() {}

    override fun initData() {}
}