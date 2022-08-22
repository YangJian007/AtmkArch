package com.atmk.iot.bz_device.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.atmk.iot.bz_device.R
import com.atmk.base.aop.Log
import com.atmk.base.aop.SingleClick
import com.atmk.base.app.AppActivity
import com.atmk.iot.bz_device.api.AddDeviceApi
import com.atmk.iot.bz_device.api.ProductApi
import com.atmk.base.http.model.HttpData
import com.atmk.iot.bz_device.utils.OCRUtils
import com.atmk.base.other.PermissionCallback
import com.atmk.base.ui.dialog.MenuDialog
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.AccessToken
import com.baidu.ocr.ui.camera.CameraActivity
import com.hjq.bar.TitleBar
import com.hjq.base.BaseDialog
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.hjq.toast.ToastUtils
import org.angmarch.views.NiceSpinner

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 可进行拷贝的副本
 */
class EditActivity : AppActivity() {

    companion object {
        const val INTENT_KEY_IN_Status: String = "status"
        const val INTENT_KEY_IN_PRODUCT_NAME: String = "productName"
        const val INTENT_KEY_IN_PRODUCT_ID: String = "productId"
        const val INTENT_KEY_IN_ID: String = "id"
        const val INTENT_KEY_IN_NAME: String = "name"
        const val INTENT_KEY_IN_DESCRIBE: String = "describe"

        @Log
        fun start(
            context: Context,
            status: Boolean?,
            id: String?,
            productName: String?,
            productId: String?,
            name: String?,
            describe: String?
        ) {
            val intent = Intent(context, EditActivity::class.java)
            intent.putExtra(INTENT_KEY_IN_Status, status)
            intent.putExtra(INTENT_KEY_IN_PRODUCT_NAME, productName)
            intent.putExtra(INTENT_KEY_IN_ID, id)
            intent.putExtra(INTENT_KEY_IN_PRODUCT_ID, productId)
            intent.putExtra(INTENT_KEY_IN_NAME, name)
            intent.putExtra(INTENT_KEY_IN_DESCRIBE, describe)
            context.startActivity(intent)
        }
    }

    private val atvId: EditText? by lazy { findViewById(R.id.atv_id) }
    private val llId: LinearLayout? by lazy { findViewById(R.id.ll_id) }

    private val atvName: AppCompatEditText? by lazy { findViewById(R.id.atv_name) }

    //    private val atvOrgin: NiceSpinner? by lazy { findViewById(R.id.atv_orgin) }
    private val atvExplain: AppCompatEditText? by lazy { findViewById(R.id.atv_explain) }
    private val atvProduct: NiceSpinner? by lazy { findViewById(R.id.atv_product) }
    private val tbTitle: TitleBar? by lazy { findViewById(R.id.tb_title) }
    private val zbarView: TextView? by lazy { findViewById(R.id.zbar) }
    private val tvCommit: AppCompatTextView? by lazy { findViewById(R.id.tv_commit) }
    private val tvCancle: AppCompatTextView? by lazy { findViewById(R.id.tv_cancle) }

    private val data = ArrayList<String>()
    private val idList = ArrayList<String>()
    private var product: String? = null

    override fun getLayoutId(): Int {
        return R.layout.device_dialog
    }

    override fun initView() {
        getPersission()
        //获取产品类型
        getProductType()
        atvProduct?.setOnClickListener(this)
        tvCommit?.setOnClickListener(this)
        zbarView?.setOnClickListener(this)
        tvCancle?.setOnClickListener(this)
        zbarView?.visibility = View.VISIBLE

        // 请选择您的初始化方式
        initAccessToken()
    }

    private var hasGotToken = false

    /**
     * 以license文件方式初始化
     */
    private fun initAccessToken() {
        OCR.getInstance(applicationContext).initAccessToken(object : OnResultListener<AccessToken> {
            override fun onResult(accessToken: AccessToken) {
                val token = accessToken.accessToken
                hasGotToken = true
            }

            override fun onError(error: OCRError) {
                error.printStackTrace()
                toast("licence方式获取token失败" + error.message)
            }
        }, applicationContext)
    }


    override fun onLeftClick(view: View) {
        super.onLeftClick(view)
        finish()
    }

    override fun onResume() {
        super.onResume()
        val status = intent?.getBooleanExtra(INTENT_KEY_IN_Status, false)
        //新建
        if (status == true) {
            tbTitle?.title = "新建设备"
            atvId?.isFocusable = true
            atvId?.isFocusableInTouchMode = true
            atvId?.requestFocus()
            zbarView?.visibility = View.VISIBLE
        } else {
            tbTitle?.title = "编辑设备"
            zbarView?.visibility = View.GONE
            atvId?.isFocusable = false
            atvId?.isFocusableInTouchMode = false
        }

    }

    override fun initData() {
        atvId?.setText(intent?.getStringExtra(INTENT_KEY_IN_ID))
        atvProduct?.setText(intent?.getStringExtra(INTENT_KEY_IN_PRODUCT_NAME))
        atvExplain?.setText(intent?.getStringExtra(INTENT_KEY_IN_DESCRIBE))
        atvName?.setText(intent?.getStringExtra(INTENT_KEY_IN_NAME))
        product = intent?.getStringExtra(INTENT_KEY_IN_PRODUCT_ID)
    }

    private fun getPersission() {
        XXPermissions.with(getContext())
            .permission(Permission.CAMERA)
            .permission(Permission.READ_EXTERNAL_STORAGE)
            .request(object : PermissionCallback() {
                override fun onGranted(
                    permissions: MutableList<String?>?,
                    all: Boolean
                ) {
                    if (!all) {
                        ToastUtils.show("获取部分权限成功，但部分权限未正常授予")
                        XXPermissions.startPermissionActivity(getContext(), permissions);
                    }
                }

                override fun onDenied(permissions: MutableList<String>, never: Boolean) {
                    super.onDenied(permissions, never)
                    if (never) {
                        ToastUtils.show("被永久拒绝授权，请手动授予录音和日历权限")
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.startPermissionActivity(getContext(), permissions);
                    } else {
                        ToastUtils.show("获取录音和日历权限失败")
                    }
                }
            })
    }

    private var resultBean: List<ProductApi.ResultBean?>? = null
    private fun getProductType() {
        data.clear()
        idList.clear()
        EasyHttp.get(this)
            .api(ProductApi())
            .request(object : OnHttpListener<HttpData<List<ProductApi.ResultBean?>>> {
                override fun onSucceed(result: HttpData<List<ProductApi.ResultBean?>>?) {
                    resultBean = result?.getData()
                    for (i in 0 until resultBean?.size!!) {
                        data.add(resultBean?.get(i)?.name.toString())
                        idList.add(resultBean?.get(i)?.id.toString())
                    }
                }

                override fun onFail(e: java.lang.Exception?) {
                    toast(text = e?.message)
                }
            })
    }

    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.atv_product -> {
                // 底部选择框
                MenuDialog.Builder(this)
                    .setList(data)
                    .setListener(object : MenuDialog.OnListener<String> {
                        override fun onSelected(dialog: BaseDialog?, position: Int, data: String) {
                            atvProduct?.setText(data)
                            product = idList?.get(position)!!
                            atvProduct?.clearFocus()
                        }

                        override fun onCancel(dialog: BaseDialog?) {
                            toast("取消了")
                        }
                    })
                    .show()
            }
            R.id.tv_commit -> {
                EasyHttp.patch(this)
                    .api(AddDeviceApi().apply {
                        id = atvId?.text?.toString()?.trim()
                        name = atvName?.text?.toString()?.trim()
                        productName = atvProduct?.text?.toString()?.trim()
                        productId = product
                        describe = atvExplain?.text?.toString()?.trim()
                        state?.let {
                            it[0]?.text = "未激活"
                            it[0]?.value = "notActive"
                        }
                    })
                    .request(object : OnHttpListener<HttpData<List<AddDeviceApi.ResultBean?>>> {
                        override fun onSucceed(result: HttpData<List<AddDeviceApi.ResultBean?>>?) {
                            toast(text = "编辑成功")
                            finish()
                        }

                        override fun onFail(e: java.lang.Exception?) {
                            toast(text = e?.message)
                        }

                    })
            }

            R.id.tv_cancle -> {
                finish()
            }
            R.id.zbar -> {
                //二维码扫描
                /* val intent = Intent(this, ScanActivity::class.java)
                  startActivityForResult(intent, 1001)*/
                //文字识别
                if (!checkTokenStatus()) {
                    return
                }
                val intent = Intent(
                    this@EditActivity,
                    CameraActivity::class.java
                )
                intent.putExtra(
                    CameraActivity.KEY_OUTPUT_FILE_PATH,
                    OCRUtils.getSaveFile(applicationContext).absolutePath
                )
                intent.putExtra(
                    CameraActivity.KEY_CONTENT_TYPE,
                    CameraActivity.CONTENT_TYPE_GENERAL
                )
                startActivityForResult(
                    intent,
                    107
                )
            }
        }
    }

    private fun checkTokenStatus(): Boolean {
        if (!hasGotToken) {
            Toast.makeText(applicationContext, "token还未成功获取", Toast.LENGTH_LONG).show()
        }
        return hasGotToken
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringExtra("result")
            atvId?.setText(result?.toString()!!)
        }

        // 识别成功回调，通用文字识别（高精度版）
        if (requestCode == 107 && resultCode == RESULT_OK) {
            OCRUtils.recAccurateBasic(
                applicationContext, OCRUtils.getSaveFile(applicationContext).getAbsolutePath(),
                object : OCRUtils.ServiceListener {
                    override fun onResult(result: List<String>) {
                        when (result.size) {
                            0 -> {
                                toast("未识别到符合条件的字符串")
                            }
                            1 -> {
                                atvId?.setText(result[0])
                                atvId?.setSelection(result[0].length)
                            }
                            else -> {
                                // 居中选择框
                                MenuDialog.Builder(this@EditActivity)
                                    .setGravity(Gravity.CENTER)
                                    .setList(result.toMutableList())
                                    .setListener(object : MenuDialog.OnListener<String> {

                                        override fun onSelected(
                                            dialog: BaseDialog?,
                                            position: Int,
                                            data: String
                                        ) {
                                            atvId?.setText(data)
                                            atvId?.setSelection(data.length)
                                        }

                                        override fun onCancel(dialog: BaseDialog?) {
                                        }
                                    })
                                    .show()

                            }
                        }

                    }
                })
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        // 释放内存资源
        OCR.getInstance(applicationContext).release()
    }
}
