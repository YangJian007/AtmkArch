package com.atmk.iot.bz_device.ui.activity

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.atmk.iot.bz_device.R
import com.atmk.base.aop.Log
import com.atmk.base.app.AppActivity
import com.atmk.base.app.AppFragment
import com.atmk.base.ui.adapter.TabAdapter
import com.atmk.iot.bz_device.ui.fragment.DeviceFunFragment
import com.atmk.iot.bz_device.ui.fragment.LogManagerFragment
import com.atmk.iot.bz_device.ui.fragment.RunStateFragment
import com.atmk.iot.bz_device.ui.fragment.WarnSettingFragment
import com.hjq.base.FragmentPagerAdapter

/**
 *    desc   : 设备详情页面
 */
class DeviceDetailActivity : AppActivity(), TabAdapter.OnTabListener,
    ViewPager.OnPageChangeListener {

    companion object {

        const val INTENT_KEY_IN_DEVICE_ID: String = "deviceId"
        const val INTENT_KEY_IN_DEVICE_NAME: String = "deviceName"
        const val INTENT_KEY_IN_PRODUCT_TYPE: String = "productType"

        @Log
        fun start(context: Context, deviceId: String, deviceName: String, productType: String) {
            val intent = Intent(context, DeviceDetailActivity::class.java)
            intent.putExtra(INTENT_KEY_IN_DEVICE_ID, deviceId)
            intent.putExtra(INTENT_KEY_IN_DEVICE_NAME, deviceName)
            intent.putExtra(INTENT_KEY_IN_PRODUCT_TYPE, productType)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.device_detail_activity
    }

    private val tabView: RecyclerView? by lazy { findViewById(R.id.rv_tab) }
    private val viewPager: ViewPager? by lazy { findViewById(R.id.vp) }
    private lateinit var tabAdapter: TabAdapter
    private lateinit var pagerAdapter: FragmentPagerAdapter<AppFragment<*>>

    override fun initView() {
        title = getString(INTENT_KEY_IN_DEVICE_NAME)
        pagerAdapter = FragmentPagerAdapter<AppFragment<*>>(this).apply {
            addFragment(RunStateFragment.newInstance(), "运行状态")
            addFragment(DeviceFunFragment.newInstance(), "设备功能")
            addFragment(LogManagerFragment.newInstance(), "日志管理")
            addFragment(WarnSettingFragment.newInstance(), "警告设置")
        }
        viewPager?.adapter = pagerAdapter
        viewPager?.addOnPageChangeListener(this)
        tabAdapter = TabAdapter(this, fixTabLayoutId = R.layout.tab_item_design_device_detail)
        tabView?.adapter = tabAdapter
    }

    override fun isStatusBarDarkFont(): Boolean {
        return false
    }

    override fun initData() {
        tabAdapter.apply {
            addItem("运行状态")
            addItem("设备功能")
            addItem("日志管理")
            addItem("警告设置")
            setOnTabListener(this@DeviceDetailActivity)
        }

    }

    /**
     * [TabAdapter.OnTabListener]
     */
    override fun onTabSelected(recyclerView: RecyclerView?, position: Int): Boolean {
        viewPager?.currentItem = position
        return true
    }

    /**
     * [ViewPager.OnPageChangeListener]
     */
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        tabAdapter.setSelectedPosition(position)
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager?.adapter = null
        viewPager?.removeOnPageChangeListener(this)
        tabAdapter.setOnTabListener(null)
    }
}