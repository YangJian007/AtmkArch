package com.atmk.iot.bz_device.ui.fragment

import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.atmk.base.action.StatusAction
import com.atmk.base.aop.SingleClick
import com.atmk.base.app.AppActivity
import com.atmk.base.app.TitleBarFragment
import com.atmk.base.http.model.HttpData
import com.atmk.base.ui.dialog.MenuDialog
import com.atmk.base.ui.dialog.TipsDialog
import com.atmk.base.ui.dialog.WaitDialog
import com.atmk.base.widget.StatusLayout
import com.atmk.iot.bz_device.R
import com.atmk.iot.bz_device.api.DeviceDetailApi
import com.atmk.iot.bz_device.api.SendCMDApi
import com.atmk.iot.bz_device.ui.activity.DeviceDetailActivity
import com.atmk.iot.bz_device.ui.adapter.FunctionInputAdapter
import com.google.gson.Gson
import com.hjq.base.BaseDialog
import com.hjq.base.action.AnimAction
import com.hjq.http.EasyHttp
import com.hjq.http.body.JsonBody
import com.hjq.http.listener.OnHttpListener
import com.hjq.shape.view.ShapeButton
import com.hjq.widget.layout.SettingBar
import com.hjq.widget.layout.WrapRecyclerView
import okhttp3.Call

/**
 *    设备详情-设备功能Fm
 */
class DeviceFunFragment : TitleBarFragment<AppActivity>(), FunctionInputAdapter.AddParamInterface,
    StatusAction {


    companion object {

        fun newInstance(): DeviceFunFragment {
            return DeviceFunFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.device_fun_fragment1
    }


    private val niceSpinner: SettingBar? by lazy { findViewById(R.id.nice_spinner) }
    private val hl_status_hint: StatusLayout? by lazy { findViewById(R.id.hl_status_hint) }
    private val recyclerView: WrapRecyclerView? by lazy { findViewById(R.id.rv) }
    private val btnRun: ShapeButton? by lazy { findViewById(R.id.btn_run) }
    private val btnClear: ShapeButton? by lazy { findViewById(R.id.btn_clear) }
    private val tvResult: TextView? by lazy { findViewById(R.id.tv_result) }
    private var adapter: FunctionInputAdapter? = null
    private lateinit var functions: List<DeviceDetailApi.Function>
    private val params = mutableMapOf<String, String?>()
    private var functionIndex: Int = 0

    /** 等待对话框 */
    private var waitDialog: BaseDialog? = null

    override fun initView() {

        //初始化功能入参rv
        adapter = FunctionInputAdapter(getAttachActivity()!!, this)
        recyclerView?.adapter = adapter
        //初始化点击事件
        setOnClickListener(btnRun, btnClear, niceSpinner)
    }

    override fun initData() {
        //获取功能列表
        EasyHttp.get(viewLifecycleOwner)
            .api(DeviceDetailApi(getAttachActivity()!!.getString(DeviceDetailActivity.INTENT_KEY_IN_DEVICE_ID)!!))
            .request(object : OnHttpListener<HttpData<DeviceDetailApi.Bean?>> {
                override fun onSucceed(result: HttpData<DeviceDetailApi.Bean?>?) {
                    //niceSpinner赋值
                    functions = Gson().fromJson(
                        result?.getData()?.metadata,
                        DeviceDetailApi.MetaData::class.java
                    ).functions
                    if (functions.isEmpty()) {
                        return
                    }
                    //功能列表复制
                    niceSpinner?.setRightText(functions[0].name)
                    //参数列表赋值
                    adapter?.clearData()
                    adapter?.setData(functions[0].inputs.toMutableList())
                    if (functions[0].inputs.isEmpty()) {
                        showLayout(null, "暂无参数", null)
                    } else {
                        showComplete()
                    }
                }

                override fun onFail(e: java.lang.Exception?) {
                    toast("获取设备功能列表失败")
                }
            })

    }

    /*
    * 参数列表添加
     */
    override fun addParam(key: String, value: String?) {
        params[key] = value
    }

    @SingleClick
    override fun onClick(view: View) {
        if (view === btnRun) {

            //发送指令
            Gson().toJson(params)
            EasyHttp.post(viewLifecycleOwner)
                .api(
                    SendCMDApi(
                        getAttachActivity()!!.getString(DeviceDetailActivity.INTENT_KEY_IN_DEVICE_ID)!!,
                        functions[functionIndex].id
                    )
                )
                .body(JsonBody(Gson().toJson(params)))
                .request(object : OnHttpListener<HttpData<SendCMDApi.Bean?>> {
                    override fun onStart(call: Call?) {
                        //弹框
                        if (waitDialog == null) {
                            waitDialog = WaitDialog.Builder(requireContext()) // 消息文本可以不用填写
                                .setMessage("指令下发中")
                                .create()
                        }
                        waitDialog?.show()
                        //清空结果
                        tvResult?.text = null
                    }

                    override fun onSucceed(result: HttpData<SendCMDApi.Bean?>?) {
                        tvResult?.text = result?.getData().toString()
                        TipsDialog.Builder(requireContext())
                            .setIcon(TipsDialog.ICON_FINISH)
                            .setMessage("指令下发完成")
                            .show()
                    }

                    override fun onFail(e: java.lang.Exception?) {
                        // 失败对话框
                        TipsDialog.Builder(requireContext())
                            .setIcon(TipsDialog.ICON_ERROR)
                            .setMessage("指令下发失败")
                            .show()
                        tvResult?.text = "指令下发失败！"
                    }

                    override fun onEnd(call: Call?) {
                        waitDialog?.dismiss()
                    }
                })
            return
        }
        if (view === btnClear) {
            tvResult?.text = null
            return
        }
        if (view === niceSpinner) {
            // 底部选择框
            MenuDialog.Builder(requireContext()) // 设置点击按钮后不关闭对话框
                //.setAutoDismiss(false)
                .setList(functions.map { it.name }.toMutableList())
                .setListener(object : MenuDialog.OnListener<String> {

                    override fun onSelected(dialog: BaseDialog?, position: Int, data: String) {
                        functionIndex = position
                        niceSpinner?.setRightText(functions[functionIndex].name)
                        //清空上报参数
                        params.clear()
                        //参数列表赋值
                        adapter?.clearData()
                        adapter?.setData(functions[functionIndex].inputs.toMutableList())
                        if (functions[functionIndex].inputs.isEmpty()) {
                            showLayout(
                                null, "暂无参数", null
                            )
                        } else {
                            showComplete()
                        }
                    }
                })
                .setGravity(Gravity.BOTTOM)
                .setAnimStyle(AnimAction.ANIM_BOTTOM)
                .show()
        }
    }

    override fun getStatusLayout(): StatusLayout? {
        return hl_status_hint
    }

}