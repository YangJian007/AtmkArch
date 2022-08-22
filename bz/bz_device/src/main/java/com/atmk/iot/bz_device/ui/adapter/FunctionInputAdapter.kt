package com.atmk.iot.bz_device.ui.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.atmk.iot.bz_device.R
import com.atmk.base.app.AppAdapter
import com.atmk.iot.bz_device.api.DeviceDetailApi
import com.hjq.shape.layout.ShapeLinearLayout
import com.hjq.shape.view.ShapeEditText
import com.jaredrummler.materialspinner.MaterialSpinner
import java.util.*


/**
 *    desc   : 设备功能-输入参数
 */
class FunctionInputAdapter constructor(
    context: Context,
    var addParamListener: AddParamInterface
) : AppAdapter<DeviceDetailApi.Input>(context) {

    interface AddParamInterface {
        fun addParam(key: String, value: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder()
    }

    inner class ViewHolder : AppViewHolder(R.layout.item_function_param) {

        private val tvKey: TextView? by lazy { findViewById(R.id.tv_key) }
        private val etValue: ShapeEditText? by lazy { findViewById(R.id.et_value) }
        private val spValue: MaterialSpinner? by lazy { findViewById(R.id.sp_value) }
        private val llSp: ShapeLinearLayout? by lazy { findViewById(R.id.ll_sp) }

        override fun onBindView(position: Int) {
            val bean = getItem(position)
            tvKey?.text = bean.name
            when (bean.valueType.type) {
                "enum" -> {
                    llSp?.visibility = View.VISIBLE
                    etValue?.visibility = View.GONE
                   val elements = bean.valueType.elements.map { it.text }
                    spValue?.setItems(elements)
                    spValue?.setOnItemSelectedListener { view, position, id, item ->
                        val value = bean.valueType.elements[position].value
                        addParamListener.addParam(bean.id, value)
                    }

//                    spValue?.attachDataSource(elements)
//                    spValue?.setOnSpinnerItemSelectedListener { _, _, position, _ ->
//                        val value = bean.valueType.elements[position].value
//                        addParamListener.addParam(bean.id, value)
//                    }
                    try {
                        addParamListener.addParam(bean.id, bean.valueType.elements[0].value)
                    } catch (e: Exception) {

                    }
                }
                else -> {
                    llSp?.visibility = View.GONE
                    etValue?.visibility = View.VISIBLE

                    etValue?.setText(null)
                    addParamListener.addParam(bean.id, null)

                    etValue?.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                        }

                        override fun afterTextChanged(s: Editable?) {
                            addParamListener.addParam(bean.id, s.toString().trim())
                        }
                    })
                }
            }

        }
    }
}