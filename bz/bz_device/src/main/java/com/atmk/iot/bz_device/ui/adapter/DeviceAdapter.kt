package com.atmk.iot.bz_device.ui.adapter

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.LifecycleOwner
import com.atmk.iot.bz_device.R
import com.atmk.base.app.AppAdapter
import com.atmk.base.http.model.HttpData
import com.atmk.iot.bz_device.ui.activity.EditActivity

import com.atmk.iot.bz_device.ui.activity.VideoPlayActivity
import com.atmk.base.ui.dialog.MessageDialog
import com.atmk.iot.bz_device.api.*
import com.atmk.iot.bz_device.ui.fragment.HomeFragment
import com.hjq.base.BaseDialog
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.hjq.toast.ToastUtils
import java.text.SimpleDateFormat

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/11/05
 *    desc   : 可进行拷贝的副本
 */
class DeviceAdapter(context1: Context, homeFragment: HomeFragment) : AppAdapter<DeviceApi.Data?>(context1) {
//    var context:Context=context1
    var fragment:HomeFragment=homeFragment
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.device_item) {
        private val imageView: AppCompatImageView? by lazy { findViewById(R.id.aiv_device) }
        private val atvDeviceName: AppCompatTextView? by lazy { findViewById(R.id.atv_device_name) }
        private val atvProductName: AppCompatTextView? by lazy { findViewById(R.id.atv_product_name) }
        private val atvDeviceId: AppCompatTextView? by lazy { findViewById(R.id.atv_device_id) }
        private val atvDeviceDate: AppCompatTextView? by lazy { findViewById(R.id.atv_device_date) }
        private val atvDeviceStatus: AppCompatTextView? by lazy { findViewById(R.id.atv_device_status) }
        private val atvPlay: AppCompatTextView? by lazy { findViewById(R.id.atv_play) }
        private val atvEdit: AppCompatTextView? by lazy { findViewById(R.id.atv_edit) }
        private val atvOpen: AppCompatTextView? by lazy { findViewById(R.id.atv_open) }
        private val atvDel: AppCompatTextView? by lazy { findViewById(R.id.atv_del) }
        private val atvClose: AppCompatTextView? by lazy { findViewById(R.id.atv_close) }
        private val llOpen: LinearLayout? by lazy { findViewById(R.id.ll_open) }


        override fun onBindView(position: Int) {
            val bean = getItem(position)
            atvDeviceName?.text = "设备名称：" + bean?.name
            atvProductName?.text = "产品名称：" + bean?.productName
            atvDeviceId?.text = "设备ID：" + bean?.id
            atvDeviceDate?.text = "设备上报：" + SimpleDateFormat("yyyy-MM-dd kk:mm:ss").format(bean?.createTime)
            if (bean?.state?.value.equals("online")) {
                atvDeviceStatus?.setTextColor(Color.parseColor("#16B86A"))
                atvDeviceStatus?.setText("在线")
                imageView?.setImageResource(R.mipmap.ic_inline1)
                atvDeviceStatus?.setBackgroundResource(R.drawable.bg_in_status)
                atvClose?.visibility= View.VISIBLE
                llOpen?.visibility = View.GONE
            }
            else if(bean?.state?.value.equals("offline")) {
                atvDeviceStatus?.setTextColor(Color.parseColor("#333333"))
                atvDeviceStatus?.setText("离线")
                imageView?.setImageResource(R.mipmap.ic_offline1)
                atvDeviceStatus?.setBackgroundResource(R.drawable.bg_status)
                atvClose?.visibility= View.VISIBLE
                llOpen?.visibility = View.GONE
            } else {
//                notActive
                atvDeviceStatus?.setTextColor(Color.parseColor("#333333"))
                atvDeviceStatus?.setText("未启用")
                imageView?.setImageResource(R.mipmap.ic_offline1)
                atvDeviceStatus?.setBackgroundResource(R.drawable.bg_status)
                atvClose?.visibility= View.GONE
                llOpen?.visibility = View.VISIBLE
            }
            //编辑
            atvEdit?.setOnClickListener {
                EditActivity.start(
                       getContext(),
                        false,
                        bean?.id,
                        bean?.productName,
                        bean?.productId,
                        bean?.name,
                        bean?.describe
                )
//                EditDialog.Builder(context)
//                        .setType(false)
//                        .setData(bean?.id,bean?.productName,bean?.productId,bean?.name,bean?.describe)
//                        .setListener(object : EditDialog.OnListener {
//                            override fun onConfirm(dialog: BaseDialog?, id1: String?, name1: String?, explain: String?, product: String?, productId1: String?) {
//                                EasyHttp.patch(context)
//                                        .api(AddDeviceApi().apply {
//                                            id= id1!!
//                                            name= name1!!
//                                            productName=product!!
//                                            describe= explain!!
//                                            productId= productId1!!
//                                            state?.let {
//                                                it[0]?.text="未激活"
//                                                it[0]?.value="notActive"
//                                            }
//                                        })
//                                        .request(object : OnHttpListener<HttpData<List<AddDeviceApi.ResultBean?>>> {
//                                            override fun onSucceed(result: HttpData<List<AddDeviceApi.ResultBean?>>?) {
//                                                context. toast(text = "编辑成功")
//                                                fragment?.refresh()
//                                            }
//                                            override fun onFail(e: java.lang.Exception?) {
//                                                context.   toast(text = e?.message)
//                                            }
//
//                                        })
//
//                            }
//
//                            override fun onCancel(dialog: BaseDialog?) {
//                                super.onCancel(dialog)
//                                dialog?.dismiss()
//                            }
//                        }).show()
            }
            //删除
            atvDel?.setOnClickListener {
                MessageDialog.Builder(getContext())
                        .setMessage("确认删除？")
                        .setListener(object :MessageDialog.OnListener{
                            override fun onConfirm(dialog: BaseDialog?) {
                                EasyHttp.delete(getContext() as LifecycleOwner)
                                        .api(DeleteApi(bean?.id))
                                        .request(object : OnHttpListener<HttpData<List<String?>>> {
                                            override fun onSucceed(result: HttpData<List<String?>>?) {
                                              ToastUtils.show("删除成功")
                                                fragment?.refresh()
                                            }
                                            override fun onFail(e: java.lang.Exception?) {
                                                ToastUtils.show( e?.message)
                                            }
                                        })
                            }

                            override fun onCancel(dialog: BaseDialog?) {

                            }

                        }).show()
            }
            //播放
            if (getItem(position)?.productId?.contains("gb28181")!!) {
                atvPlay?.visibility = View.VISIBLE
                atvPlay?.setOnClickListener {
                    EasyHttp.get(getContext() as LifecycleOwner)
                        .api(VideoUrlApi(getItem(position)?.id!!))
                        .request(object : OnHttpListener<HttpData<VideoUrlApi.Bean?>> {
                            override fun onSucceed(result: HttpData<VideoUrlApi.Bean?>?) {

                                VideoPlayActivity.Builder()
                                    .setVideoSource(result?.getData()?.playUrl)
                                    .setActivityOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                                    .start(getContext()!!)
                            }

                            override fun onFail(e: java.lang.Exception?) {
                                Toast.makeText(getContext()!!, "获取视频链接失败", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        })
                }
            } else {
                atvPlay?.visibility=View.GONE
            }

            //禁用
            atvClose?.setOnClickListener {
                MessageDialog.Builder(getContext())
                        .setMessage("确认禁用？")
                        .setListener(object :MessageDialog.OnListener{
                            override fun onConfirm(dialog: BaseDialog?) {
                                EasyHttp.post(getContext() as LifecycleOwner)
                                        .api(DisAbleApi(bean?.id))
                                        .request(object : OnHttpListener<HttpData<List<String?>>> {
                                            override fun onSucceed(result: HttpData<List<String?>>?) {
                                                ToastUtils.show("禁用成功")
                                                fragment?.refresh()
                                            }
                                            override fun onFail(e: java.lang.Exception?) {
                                                ToastUtils.show(e?.message)
                                            }
                                        })
                            }

                            override fun onCancel(dialog: BaseDialog?) {

                            }

                        }).show()
            }
            //启用
            atvOpen?.setOnClickListener {
                MessageDialog.Builder(getContext())
                        .setMessage("确认启用？")
                        .setListener(object :MessageDialog.OnListener{
                            override fun onConfirm(dialog: BaseDialog?) {
                                EasyHttp.post(getContext() as LifecycleOwner)
                                        .api(OnAbleApi(bean?.id))
                                        .request(object : OnHttpListener<HttpData<List<String?>>> {
                                            override fun onSucceed(result: HttpData<List<String?>>?) {
                                                ToastUtils.show("启用成功")
                                                fragment?.refresh()
                                            }
                                            override fun onFail(e: java.lang.Exception?) {
                                                ToastUtils.show( e?.message)
                                            }
                                        })
                            }

                            override fun onCancel(dialog: BaseDialog?) {

                            }

                        }).show()
            }
        }
    }


}

