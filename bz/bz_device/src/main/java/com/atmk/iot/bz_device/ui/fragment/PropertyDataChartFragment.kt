package com.atmk.iot.bz_device.ui.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import com.atmk.iot.bz_device.R
import com.atmk.base.app.AppActivity
import com.atmk.base.app.TitleBarFragment
import com.atmk.iot.bz_device.api.PropertyHisData
import com.atmk.base.http.model.HttpData
import com.atmk.iot.bz_device.ui.activity.PropertyDetailActivity
import com.atmk.iot.bz_device.ui.view.MyMarkerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.hjq.base.util.TimeUtils
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import java.util.*

/**
 * @author 杨剑
 * @fileName
 * @date 2022-08-01
 * @describe  属性详情图表
 * @changeUser
 * @changTime
 */
class PropertyDataChartFragment : TitleBarFragment<AppActivity>(),
    PropertyDetailActivity.DataCallBack {

    companion object {
        private const val INTENT_KEY_START_DATE: String = "startDate"
        private const val INTENT_KEY_END_DATE: String = "endDate"

        fun newInstance(startDate: String, endDate: String): PropertyDataChartFragment {
            val fragment = PropertyDataChartFragment()
            val bundle = Bundle()
            bundle.putString(INTENT_KEY_START_DATE, startDate)
            bundle.putString(INTENT_KEY_END_DATE, endDate)
            fragment.arguments = bundle
            return fragment

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val propertyDetailActivity = getAttachActivity() as PropertyDetailActivity
        propertyDetailActivity.mDataCallBacks.add(this)
    }

    override fun getLayoutId() = R.layout.property_data_chart_fragment


    private val lineChart: LineChart? by lazy { findViewById(R.id.chart) }
    private var start = ""
    private var end = ""
    private var num = 0
    private var size = 10000

    override fun initView() {


    }

    override fun initData() {
        start = getString(INTENT_KEY_START_DATE)!!
        end = getString(INTENT_KEY_END_DATE)!!
        getData()

    }

    private fun getData() {

        EasyHttp.get(viewLifecycleOwner)
            .api(PropertyHisData().apply {
                deviceId =
                    getAttachActivity()!!.getString(PropertyDetailActivity.INTENT_KEY_IN_DEVICE_ID)!!
                propertyName =
                    getAttachActivity()!!.getString(PropertyDetailActivity.INTENT_KEY_IN_PROPERTY)!!
                startDate = start
                endDate = end
                pageNum = num
                pageSize = size
            })
            .request(object : OnHttpListener<HttpData<PropertyHisData.Bean?>> {
                override fun onSucceed(result: HttpData<PropertyHisData.Bean?>?) {
                    val xList=result?.getData()?.data?.map { TimeUtils.millis2String(it.createTime).substring(5) }
                    val yList=result?.getData()?.data?.map { it.numberValue.toFloat() }
                    initLineChart(yList!!.reversed(),xList!!.reversed())
                }

                override fun onFail(e: Exception?) {}

            })
    }

    //图表绘制
    fun initLineChart(list: List<Float>, list3: List<String?>) {
        val min1 = Collections.min(list)
        val max1 = Collections.max(list)
        //设置数据1
        val entries: MutableList<Entry> = ArrayList()
        for (i in list.indices) {
            entries.add(Entry(i.toFloat(), list[i]))
        }
        //一个LineDataSet就是一条线
        val lineDataSet = LineDataSet(entries, "")

        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER)
        lineDataSet.setCubicIntensity(0.2f)
        lineDataSet.setDrawFilled(true)
        lineDataSet.setDrawCircles(false)
        lineDataSet.setLineWidth(1.8f)
        lineDataSet.setCircleRadius(4f)
        lineDataSet.setCircleColor(Color.parseColor("#3290fc"))
        lineDataSet.setHighLightColor(Color.parseColor("#c4c4c4"))
        lineDataSet.setColor(Color.parseColor("#3290fc"))
        lineDataSet.setFillColor(Color.parseColor("#84bcfd"))
        lineDataSet.setFillAlpha(100)
        lineDataSet.setDrawHorizontalHighlightIndicator(false)
        lineDataSet.setFillFormatter(IFillFormatter { dataSet, dataProvider ->
            lineChart?.getAxisLeft()?.getAxisMinimum()!!
        })

        val data = LineData(lineDataSet)
        //无数据时显示的文字
        lineChart!!.setNoDataText("暂无数据")
        lineChart!!.isScaleXEnabled = true
        lineChart!!.isScaleYEnabled = false
        //折线图不显示数值
        data.setDrawValues(false)
        //得到X轴
        val xAxis = lineChart!!.xAxis
        //设置X轴的位置（默认在上方)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        //设置X轴坐标之间的最小间隔
        xAxis.granularity = 1f
        //隐藏网格线虚线
        xAxis.setDrawGridLines(false)
        // 标签倾斜为0
        xAxis.labelRotationAngle = 90f
        //设置X轴颜色
        xAxis.textColor = Color.GRAY
        xAxis.setDrawLabels(true)
        xAxis.labelCount = list3.size
        xAxis.valueFormatter = IndexAxisValueFormatter(list3)

        //隐藏右侧Y轴
        lineChart!!.axisRight.isEnabled = false
        //得到Y轴
        val yAxis = lineChart!!.axisLeft
        yAxis.isGranularityEnabled = true
        //设置Y轴坐标之间的最小间隔
        yAxis.granularity = 1f
        //设置y轴的刻度数量
        yAxis.setLabelCount(10, false)
        //设置从Y轴值
        yAxis.axisMinimum = 0f
        yAxis.axisMaximum = max1+1
        //+1:y轴多一个单位长度，为了好看
        yAxis.textColor = Color.GRAY
        //显示网格线虚线
        yAxis.enableGridDashedLine(10f, 10f, 0f)
        // set an alternative background color
        lineChart!!.setBackgroundColor(Color.WHITE)
        lineChart!!.extraBottomOffset=10f
        // create marker to display box when values are selected
        val mv = MyMarkerView(activity, R.layout.custom_marker_view)
        // Set the marker to the chart
        mv.setChartView(lineChart)
        lineChart!!.marker = mv
        //图例：得到Lengend
        val legend = lineChart!!.legend
        //隐藏Lengend
        legend.isEnabled = false
        //隐藏描述
        val description = Description()
        description.isEnabled = false
        lineChart!!.description = description
        //设置数据
        lineChart!!.data = data
        //图标刷新
        lineChart!!.invalidate()
    }


    /**
     * [PropertyDetailActivity.DataCallBack]
     */
    override fun acceptData(startDate: String, endDate: String) {
        start = startDate
        end = endDate
        getData()
    }


}