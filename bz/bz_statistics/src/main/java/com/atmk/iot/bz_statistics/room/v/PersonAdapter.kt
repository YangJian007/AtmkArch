package com.atmk.iot.bz_statistics.room.v

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView

import com.atmk.base.app.AppAdapter
import com.atmk.iot.bz_statistic.R
import com.atmk.iot.bz_statistics.room.m.Person


/**
 *    desc   : 设备功能-输入参数
 */
class PersonAdapter constructor(context: Context) : AppAdapter<Person>(context) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.person_list_item) {

        private val tvName: TextView? by lazy { findViewById(R.id.tv_key) }
        private val tvValue: TextView? by lazy { findViewById(R.id.tv_value) }


        override fun onBindView(position: Int) {
            val bean = getItem(position)
            tvName?.text = bean.name
            tvValue?.text=bean.age.toString()
        }
    }
}