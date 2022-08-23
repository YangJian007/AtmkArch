package com.atmk.iot.bz_device.ui.activity

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.atmk.iot.bz_device.R
import com.atmk.base.aop.Log
import com.atmk.base.aop.SingleClick
import com.atmk.base.app.AppActivity
import com.atmk.base.app.AppFragment
import com.atmk.base.ui.adapter.TabAdapter
import com.atmk.base.ui.dialog.DateDialog
import com.atmk.iot.bz_device.ui.fragment.PropertyDataChartFragment
import com.atmk.iot.bz_device.ui.fragment.PropertyDataTableFragment
import com.flyco.tablayout.SegmentTabLayout
import com.flyco.tablayout.listener.OnTabSelectListener
import com.hjq.base.BaseDialog
import com.hjq.base.FragmentPagerAdapter
import com.hjq.base.util.TimeUtils
import com.hjq.shape.view.ShapeTextView
import com.hjq.widget.layout.NoScrollViewPager
import java.text.DecimalFormat

/**
 * @author 杨剑
 * @fileName
 * @date 2022-08-01
 * @describe  属性详情
 * @changeUser
 * @changTime
 */
class PropertyDetailActivity : AppActivity(), TabAdapter.OnTabListener,
    ViewPager.OnPageChangeListener {

    //通知fragment的接口
    interface DataCallBack {
        fun acceptData(startDate: String, endDate: String)
    }

    val mDataCallBacks = mutableSetOf<DataCallBack>()

    companion object {

        const val INTENT_KEY_IN_DEVICE_ID: String = "deviceId"
        const val INTENT_KEY_IN_PROPERTY: String = "property"
        private const val INTENT_KEY_DATA_TYPE: String = "type"


        @Log
        fun start(context: Context, deviceId: String, property: String, type: String) {
            val intent = Intent(context, PropertyDetailActivity::class.java)
            intent.putExtra(INTENT_KEY_IN_DEVICE_ID, deviceId)
            intent.putExtra(INTENT_KEY_IN_PROPERTY, property)
            intent.putExtra(INTENT_KEY_DATA_TYPE, type)
            context.startActivity(intent)
        }
    }


    override fun getLayoutId() = R.layout.device_property_detail_activity

    private val tabLayout: SegmentTabLayout? by lazy { findViewById(R.id.tl) }
    private val tabView: RecyclerView? by lazy { findViewById(R.id.rv_tab) }
    private val viewPager: NoScrollViewPager? by lazy { findViewById(R.id.vp) }
    private lateinit var tabAdapter: TabAdapter
    private lateinit var pagerAdapter: FragmentPagerAdapter<AppFragment<*>>

    //请求参数
    private val tvStartTime: ShapeTextView? by lazy { findViewById(R.id.tv_start_time) }
    private val tvEndTime: ShapeTextView? by lazy { findViewById(R.id.tv_end_time) }

//        private var startDate = TimeUtils.getNowString(TimeUtils.getSafeDateFormat("yyyy-MM-dd"))
    private var startDate = TimeUtils.millis2String(
        TimeUtils.getNowMills() - 2592000000,
        TimeUtils.getSafeDateFormat("yyyy-MM-dd")
    )
    private var endDate = TimeUtils.getNowString(TimeUtils.getSafeDateFormat("yyyy-MM-dd"))

    override fun initView() {
        pagerAdapter = FragmentPagerAdapter<AppFragment<*>>(this).apply {
            addFragment(PropertyDataTableFragment.newInstance(startDate, endDate), "表格")
            if (getString(INTENT_KEY_DATA_TYPE) == "double")
                addFragment(PropertyDataChartFragment.newInstance(startDate, endDate), "图表")
        }
        viewPager?.adapter = pagerAdapter
        viewPager?.addOnPageChangeListener(this)
        tabAdapter = TabAdapter(this)
        tabView?.adapter = tabAdapter

        tvStartTime?.text = startDate
        tvEndTime?.text = endDate
        setOnClickListener(R.id.tv_start_time, R.id.tv_end_time)
    }

    override fun initData() {
        tabAdapter.apply {
            addItem("表格")
            if (getString(INTENT_KEY_DATA_TYPE) == "double")
                addItem("图表")
            setOnTabListener(this@PropertyDetailActivity)
        }

        tabLayout?.visibility =
            if (getString(INTENT_KEY_DATA_TYPE) == "double") View.VISIBLE else View.GONE
        tabLayout?.setTabData(arrayOf("表格", "图表"))
        tabLayout?.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                viewPager?.currentItem = position
            }

            override fun onTabReselect(position: Int) {

            }

        })


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
        tabLayout?.currentTab = position
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager?.adapter = null
        viewPager?.removeOnPageChangeListener(this)
        tabAdapter.setOnTabListener(null)
    }

    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_start_time -> {
                //开始时间
                DateDialog.Builder(this, 2020)
                    .setTitle(getString(R.string.device_start_date_title))
                    .setListener(object : DateDialog.OnListener {
                        override fun onSelected(
                            dialog: BaseDialog?,
                            year: Int,
                            month: Int,
                            day: Int
                        ) {
                            startDate = "$year-${DecimalFormat("00").format(month)}-${
                                DecimalFormat("00").format(day)
                            }"
                            tvStartTime?.text = startDate
                            mDataCallBacks.forEach {
                                it.acceptData(startDate, endDate)
                            }
                        }

                        override fun onCancel(dialog: BaseDialog?) {}
                    })
                    .show()

            }
            R.id.tv_end_time -> {
                //结束时间
                DateDialog.Builder(this, 2020)
                    .setTitle(getString(R.string.device_end_date_title))
                    .setListener(object : DateDialog.OnListener {
                        override fun onSelected(
                            dialog: BaseDialog?,
                            year: Int,
                            month: Int,
                            day: Int
                        ) {
                            endDate = "$year-${DecimalFormat("00").format(month)}-${
                                DecimalFormat("00").format(day)
                            }"
                            tvEndTime?.text = endDate
                            mDataCallBacks.forEach {
                                it.acceptData(startDate, endDate)
                            }
                        }

                        override fun onCancel(dialog: BaseDialog?) {}
                    })
                    .show()
            }
        }
    }


}