package com.atmk.iot.bz_device.ui.fragment

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.atmk.base.aop.SingleClick
import com.atmk.base.app.AppActivity
import com.atmk.base.app.TitleBarFragment
import com.atmk.base.http.model.HttpData
import com.atmk.base.ui.dialog.MenuDialog
import com.atmk.iot.bz_device.R
import com.atmk.iot.bz_device.api.DeviceApi
import com.atmk.iot.bz_device.api.ProductApi
import com.atmk.iot.bz_device.api.StatusApi
import com.atmk.iot.bz_device.ui.activity.DeviceDetailActivity
import com.atmk.iot.bz_device.ui.activity.EditActivity
import com.atmk.iot.bz_device.ui.adapter.DeviceAdapter
import com.hjq.base.BaseAdapter
import com.hjq.base.BaseDialog
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.hjq.widget.layout.WrapRecyclerView
import com.hjq.widget.view.ClearEditText
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener


/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 可进行拷贝的副本
 */
@Route(path = "/device/homeFm")
class HomeFragment : TitleBarFragment<AppActivity>(), OnRefreshLoadMoreListener,
    BaseAdapter.OnItemClickListener, AdapterView.OnItemSelectedListener {
    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.home_fragment
    }

    private val sbProductType: AppCompatTextView? by lazy { findViewById(R.id.sb_product_type) }
    private val etStationName: ClearEditText? by lazy { findViewById(R.id.et_station_name) }
    private val recyclerView: WrapRecyclerView? by lazy { findViewById(R.id.rv_device_list) }
    private var adapter: DeviceAdapter? = null
    private val refreshLayout: SmartRefreshLayout? by lazy { findViewById(R.id.srl_device) }
    private val atvAllCount: AppCompatTextView? by lazy { findViewById(R.id.atv_all_count) }
    private val atvOnCount: AppCompatTextView? by lazy { findViewById(R.id.atv_on_count) }
    private val atvOffCount: AppCompatTextView? by lazy { findViewById(R.id.atv_off_count) }
    private val atvNoCount: AppCompatTextView? by lazy { findViewById(R.id.atv_no_count) }
    private val fabAdd: AppCompatTextView? by lazy { findViewById(R.id.fab_add) }
    private val llAll: LinearLayout? by lazy { findViewById(R.id.ll_all) }
    private val llOnline: LinearLayout? by lazy { findViewById(R.id.ll_online) }
    private val llOffline: LinearLayout? by lazy { findViewById(R.id.ll_offline) }
    private val llNoAlive: LinearLayout? by lazy { findViewById(R.id.ll_noAlive) }
    private var data = mutableListOf<String>()
    private var idList = mutableListOf<String>()
    private var productId: String = ""
    private var name: String = ""
    private var deviceStatus: String = ""
    private val STATUS_ALL: Int = 0
    private val STATUS_ON: Int = 1
    private val STATUS_OFF: Int = 2
    private val STATUS_NOALIVE: Int = 3
    private var index: Int = 0
    private var isFresh: Boolean = false
    override fun initView() {
        adapter = DeviceAdapter(getAttachActivity()!!, this)
        adapter?.setOnItemClickListener(this)
        recyclerView?.adapter = adapter
        refreshLayout?.setEnableLoadMore(true)
        refreshLayout?.setOnRefreshLoadMoreListener(this)
        refreshLayout?.setOnRefreshListener(this)
        refreshLayout!!.autoRefresh()
        sbProductType?.setOnClickListener(this)
        fabAdd?.setOnClickListener(this)
        llAll?.setOnClickListener(this)
        llOnline?.setOnClickListener(this)
        llOffline?.setOnClickListener(this)
        llNoAlive?.setOnClickListener(this)

        etStationName?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                name = s.toString().trim()!!
                getDevice(index, productId, name, deviceStatus)
            }

        })
    }


    private var isFirst = true
    override fun onResume() {
        super.onResume()
        if (!isFirst) {
            refresh()
        } else {
            isFirst = false
        }
    }


    override fun initData() {
        //获取产品类型
        getProductType()
    }

    private fun getAllStatus(stataus: Int, productId: String) {
        EasyHttp.get(viewLifecycleOwner)
            .api(StatusApi(stataus, productId))
            .request(object : OnHttpListener<HttpData<String?>> {
                override fun onSucceed(result: HttpData<String?>?) {
                    when (stataus) {
                        0 -> {
                            atvAllCount?.text = result?.getData()
                        }
                        1 -> {
                            atvOnCount?.text = result?.getData()
                        }
                        2 -> {
                            atvOffCount?.text = result?.getData()
                        }
                        3 -> {
                            atvNoCount?.text = result?.getData()
                        }
                    }
                }

                override fun onFail(e: java.lang.Exception?) {
                    toast(text = e?.message)
                }
            })
    }

    private lateinit var dataBean: MutableList<DeviceApi.Data?>
    private fun getDevice(index: Int, productId: String, name: String, deviceStatus: String) {
        EasyHttp.get(viewLifecycleOwner)
            .api(
                DeviceApi(
                    index,
                    productId,
                    name,
                    deviceStatus
                )
            ).request(object : OnHttpListener<HttpData<DeviceApi.Beans?>> {
                override fun onSucceed(result: HttpData<DeviceApi.Beans?>?) {
                    dataBean = result?.getData()?.data?.toMutableList()!!
                    //参数列表赋值
                    if (!isFresh) {
                        adapter?.clearData()
                    }
                    adapter?.setData(dataBean)
                    refreshLayout!!.finishRefresh()
                    adapter?.notifyDataSetChanged()
                }

                override fun onFail(e: java.lang.Exception?) {
                    toast(text = e?.message)
                }
            })
    }

    fun refresh() {
        index = 0
        deviceStatus = ""
        getDevice(index, productId, name, deviceStatus)
        getAllStatus(STATUS_ALL, productId)
        getAllStatus(STATUS_ON, productId)
        getAllStatus(STATUS_OFF, productId)
        getAllStatus(STATUS_NOALIVE, productId)
    }

    private var resultBean: List<ProductApi.ResultBean?>? = null
    private fun getProductType() {
        data.clear()
        idList.clear()
        EasyHttp.get(viewLifecycleOwner)
            .api(ProductApi())
            .request(object : OnHttpListener<HttpData<List<ProductApi.ResultBean?>>> {
                override fun onSucceed(result: HttpData<List<ProductApi.ResultBean?>>?) {
                    resultBean = result?.getData()
                    resultBean?.forEach {
                        data.add(it?.name!!)
                        idList.add(it.id)
                    }


                }

                override fun onFail(e: java.lang.Exception?) {
                    toast(text = e?.message)
                }

            })
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        refresh()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        isFresh = true
        index++
        getDevice(index, productId, name, deviceStatus)
        postDelayed({
            this.refreshLayout?.finishLoadMore()
            adapter?.apply {
                addData(dataBean)
                setLastPage(getCount() >= 100)
                this@HomeFragment.refreshLayout?.setNoMoreData(isLastPage())
            }
        }, 1000)
    }

    override fun onItemClick(recyclerView: RecyclerView?, itemView: View?, position: Int) {
        DeviceDetailActivity.start(
            requireContext(),
            adapter?.getItem(position)?.id!!,
            adapter?.getItem(position)?.name!!,
            adapter?.getItem(position)?.productId!!,
        )
    }

    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.sb_product_type -> {
                // 底部选择框
                MenuDialog.Builder(requireContext())
                    .setList(data)
                    .setListener(object : MenuDialog.OnListener<String> {
                        override fun onSelected(dialog: BaseDialog?, position: Int, data: String) {
                            sbProductType?.text = data
                            productId = idList[position]
                            sbProductType?.clearFocus()
                            refresh()
                        }

                        override fun onCancel(dialog: BaseDialog?) {
                            toast("取消了")
                        }
                    })
                    .show()
            }
            R.id.fab_add -> {
                EditActivity.start(
                    getAttachActivity()!!,
                    true, "", "", "", "", ""
                )
            }
            R.id.ll_all -> {
                deviceStatus = ""
                index = 0
                getDevice(index, productId, name, deviceStatus)
            }
            R.id.ll_online -> {
                llOnline?.isEnabled = true
                deviceStatus = "online"
                index = 0
                getDevice(index, productId, name, deviceStatus)
            }
            R.id.ll_offline -> {
                llOffline?.isEnabled = true
                deviceStatus = "offline"
                getDevice(index, productId, name, deviceStatus)
            }
            R.id.ll_noAlive -> {
                llNoAlive?.isEnabled = true
                deviceStatus = "notActive"
                getDevice(index, productId, name, deviceStatus)
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun isStatusBarEnabled(): Boolean {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled()
    }


}

