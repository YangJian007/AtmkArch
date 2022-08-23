package com.atmk.iot.bz_device.ui.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.atmk.iot.bz_device.R
import com.atmk.base.app.AppAdapter
import com.atmk.iot.bz_device.api.RunStateApi


/**
 *    desc   : 设备功能-输入参数
 */
class RunStateAdapter constructor(context: Context) : AppAdapter<RunStateApi.BeanItem>(context) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.device_item_iotdetail_paralist) {

        private val tvName: TextView? by lazy { findViewById(R.id.tv_iot_name) }
        private val tvValue: TextView? by lazy { findViewById(R.id.tv_iot_value) }
        private val tvTime: TextView? by lazy { findViewById(R.id.tv_iot_time) }

        override fun onBindView(position: Int) {
            val bean = getItem(position).data
            tvName?.text = bean.value.propertyName
            tvValue?.text=bean.value.formatValue
            tvTime?.text=bean.timeString
        }
    }
}