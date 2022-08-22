package com.atmk.iot.bz_device.ui.fragment

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.atmk.base.app.AppActivity
import com.atmk.base.app.TitleBarFragment
import com.atmk.iot.bz_device.R
import com.atmk.iot.bz_device.api.RunStateApi
import com.atmk.base.http.model.HttpData
import com.atmk.iot.bz_device.ui.activity.DeviceDetailActivity
import com.atmk.iot.bz_device.ui.activity.PropertyDetailActivity
import com.atmk.iot.bz_device.ui.adapter.RunStateAdapter
import com.hjq.base.BaseAdapter
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.hjq.widget.layout.WrapRecyclerView

/**
 *    设备详情-运行状态Fm
 */
class RunStateFragment : TitleBarFragment<AppActivity>(), BaseAdapter.OnItemClickListener {

    companion object {

        fun newInstance(): RunStateFragment {
            return RunStateFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.run_state_fragment
    }

    private val recyclerView: WrapRecyclerView? by lazy { findViewById(R.id.rv) }
    private var adapter: RunStateAdapter? = null

    override fun initView() {
        //初始化功能入参rv
        adapter = RunStateAdapter(getAttachActivity()!!)
        adapter?.setOnItemClickListener(this)
        recyclerView?.adapter = adapter
    }

    override fun initData() {
        //获取参数列表
        EasyHttp.post(viewLifecycleOwner)
            .api(
                RunStateApi(
                    `object` = getAttachActivity()!!.getString(DeviceDetailActivity.INTENT_KEY_IN_PRODUCT_TYPE)!!,
                    params = RunStateApi.Params(getAttachActivity()!!.getString(DeviceDetailActivity.INTENT_KEY_IN_DEVICE_ID)!!)
                )
            )
            .request(object : OnHttpListener<HttpData<RunStateApi.Bean?>> {
                override fun onSucceed(result: HttpData<RunStateApi.Bean?>?) {
                    //参数列表赋值
                    adapter?.clearData()
                    adapter?.setData(result?.getData()!!.toMutableList())
                }

                override fun onFail(e: java.lang.Exception?) {
                    toast(e?.message)
                }
            })

    }

    /**
     * [BaseAdapter.OnItemClickListener]
     *
     * @param recyclerView      RecyclerView对象
     * @param itemView          被点击的条目对象
     * @param position          被点击的条目位置
     */
    override fun onItemClick(recyclerView: RecyclerView?, itemView: View?, position: Int) {

        /* 跳转视频播放页面
        EasyHttp.get(this)
             .api(VideoUrlApi(getAttachActivity()!!.getString(DeviceDetailActivity.INTENT_KEY_IN_DEVICE_ID)!!))
             .request(object : OnHttpListener<HttpData<VideoUrlApi.Bean?>> {
                 override fun onSucceed(result: HttpData<VideoUrlApi.Bean?>?) {

                     VideoPlayActivity.Builder()
                         .setVideoSource(result?.getData()?.playUrl)
                         .setActivityOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                         .start(getAttachActivity()!!)
                 }

                 override fun onFail(e: java.lang.Exception?) {
                     toast("获取视频链接失败")
                 }
             })*/

        PropertyDetailActivity.start(
            getAttachActivity()!!,
            adapter?.getItem(position)?.data?.value?.deviceId!!,
            adapter?.getItem(position)?.data?.value?.property!!,
            adapter?.getItem(position)?.data?.value?.type!!,
        )


    }
}