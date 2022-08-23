package com.atmk.iot.bz_device.ui.fragment


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.graphics.Color
import android.os.Bundle
import com.atmk.iot.bz_device.R
import com.atmk.base.app.AppActivity
import com.atmk.base.app.TitleBarFragment
import com.atmk.iot.bz_device.api.PropertyHisData
import com.atmk.base.http.model.HttpData
import com.atmk.iot.bz_device.ui.activity.PropertyDetailActivity
import com.bin.david.form.core.SmartTable
import com.bin.david.form.data.format.bg.BaseBackgroundFormat
import com.bin.david.form.data.style.FontStyle
import com.bin.david.form.data.table.MapTableData
import com.hjq.base.util.TimeUtils
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener


/**
 * @author 杨剑
 * @fileName
 * @date 2022-08-01
 * @describe 属性详情表格
 * @changeUser
 * @changTime
 */
class PropertyDataTableFragment : TitleBarFragment<AppActivity>(),
    PropertyDetailActivity.DataCallBack, OnRefreshLoadMoreListener {


    companion object {
        private const val INTENT_KEY_START_DATE: String = "startDate"
        private const val INTENT_KEY_END_DATE: String = "endDate"

        fun newInstance(startDate: String, endDate: String): PropertyDataTableFragment {
            val fragment = PropertyDataTableFragment()
            val bundle = Bundle()
            bundle.putString(INTENT_KEY_START_DATE, startDate)
            bundle.putString(INTENT_KEY_END_DATE, endDate)
            fragment.arguments = bundle
            return fragment

        }
    }

    private val refreshLayout: SmartRefreshLayout? by lazy { findViewById(R.id.rl_status_refresh) }
    private val table: SmartTable<Any>? by lazy { findViewById(R.id.table) }
    private var start = ""
    private var end = ""
    private var num = 0
    private var size = 20
    private val listData = mutableListOf<PropertyHisData.Data>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val propertyDetailActivity = getAttachActivity() as PropertyDetailActivity
        propertyDetailActivity.mDataCallBacks.add(this)
    }

    override fun getLayoutId() = R.layout.device_property_data_table_fragment

    override fun initView() {
        refreshLayout?.setOnRefreshLoadMoreListener(this)
        initTable()
    }

    private fun initTable() {
        table!!.config
            .setShowXSequence(false)
            .setShowYSequence(false)
            .setShowTableTitle(false)
            .setColumnTitleVerticalPadding(25)
            .setVerticalPadding(25) //设置行间距
            .setColumnTitleStyle(FontStyle(40, Color.WHITE))
            .columnTitleBackground = BaseBackgroundFormat(getAttachActivity()!!.resources.getColor(R.color.common_accent_color)) //设置列标题背景;
    }

    override fun initData() {
        start = getString(INTENT_KEY_START_DATE)!!
        end = getString(INTENT_KEY_END_DATE)!!
        getData(true)

    }

    private fun getData(isRefresh: Boolean) {
        num = if (isRefresh) 0 else ++num
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
                    if (isRefresh) refreshLayout?.finishRefresh() else refreshLayout?.finishLoadMore()
                    //判断是否还有下一页
                    val index = result?.getData()?.pageIndex!!
                    val pSize = result?.getData()?.pageSize!!
                    val total = result?.getData()?.total!!
                    val currentSize = result?.getData()?.data?.size!!
                    refreshLayout?.setNoMoreData(index * pSize + currentSize >= total)
                    //填充数据
                    adapterData(isRefresh, result?.getData()?.data!!)

                }

                override fun onFail(e: Exception?) {}

            })
    }

    private fun adapterData(isRefresh: Boolean, data: List<PropertyHisData.Data>) {
        if (isRefresh) {
            listData.clear()
        }
        listData.addAll(data)
        //组装数据
        val mapList = mutableListOf<Any>()
        listData.forEach {
            val map = linkedMapOf<String, Any>(
                "时间" to TimeUtils.millis2String(it.createTime),
                "${it.propertyName}(点击可复制)" to it.formatValue
            )
            mapList.add(map)
        }
        val tableData = MapTableData.create("", mapList)
        table!!.tableData = tableData
        //设置单元格点击事件，需表格数据不为空使用
        val columns = table!!.tableData.columns
        table!!.tableData.setOnItemClickListener { column, value, t, col, row ->
            if (col == columns.size - 1) {
                putTextIntoClip(requireContext(),value)
                toast("$value \n已成功复制到剪切板")
            }
        }

    }

    /**
     * 复制到剪贴板
     * @param context
     * @param text
     */
    fun putTextIntoClip(context: Context, text: String?) {
        val clipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        //创建ClipData对象
        val clipData = ClipData.newPlainText("HSFAppDemoClip", text)
        //添加ClipData对象到剪切板中
        clipboardManager.setPrimaryClip(clipData)
    }

    /**
     * [PropertyDetailActivity.DataCallBack]
     */
    override fun acceptData(startDate: String, endDate: String) {
        start = startDate
        end = endDate
        getData(true)
    }

    /**
     * [OnRefreshLoadMoreListener]
     */
    override fun onRefresh(refreshLayout: RefreshLayout) {
        getData(true)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        getData(false)
    }
}