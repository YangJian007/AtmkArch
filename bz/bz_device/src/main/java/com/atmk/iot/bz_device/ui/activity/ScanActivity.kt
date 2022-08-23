package com.atmk.iot.bz_device.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Vibrator
import android.util.Log
import cn.bingoogolapple.qrcode.core.BarcodeType
import cn.bingoogolapple.qrcode.core.QRCodeView
import cn.bingoogolapple.qrcode.zbar.ZBarView
import com.atmk.iot.bz_device.R
import com.atmk.base.app.AppActivity


/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 可进行拷贝的副本
 */
class ScanActivity : AppActivity(), QRCodeView.Delegate {
    private val mZBarView: ZBarView? by lazy { findViewById(R.id.zbarview) }
    override fun getLayoutId(): Int {
        return R.layout.device_scan_activity
    }

    override fun initView() {
        mZBarView?.setDelegate(this)
    }

    override fun initData() {
//        mZBarView!!.setType(BarcodeType.ONE_DIMENSION, null) // 只识别一维条码
    }
    override fun onStart() {
        super.onStart()
        mZBarView!!.startCamera() // 打开后置摄像头开始预览，但是并未开始识别

        mZBarView!!.changeToScanBarcodeStyle(); // 切换成扫描条码样式
        mZBarView!!.setType(BarcodeType.ONE_DIMENSION, null); // 只识别一维条码
        mZBarView?.getScanBoxView()?.setOnlyDecodeScanBoxArea(true); // 仅识别扫描框中的码
        mZBarView!!.startSpotAndShowRect() // 显示扫描框，并且延迟0.5秒后开始识别
    }

    override fun onScanQRCodeSuccess(result: String?) {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(2000)
        mZBarView?.startSpot()
        // 延迟0.5秒后开始识别
        postDelayed({
            Log.e("zx",result?.toString()!!)
                val resultIntent = Intent()
                resultIntent.putExtra("result", result?.toString())
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
        },2000)
    }


    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        var tipText = mZBarView!!.scanBoxView.tipText
        val ambientBrightnessTip = "\n环境过暗，请打开闪光灯"
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZBarView!!.scanBoxView.tipText = tipText + ambientBrightnessTip
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip))
                mZBarView!!.scanBoxView.tipText = tipText
            }
        }
    }

    override fun onScanQRCodeOpenCameraError() {
        Log.e("zx", "打开相机出错");
    }
    override fun onStop() {
        mZBarView?.stopCamera() // 关闭摄像头预览，并且隐藏扫描框
        super.onStop()
    }

    override fun onDestroy() {
        mZBarView?.onDestroy() // 销毁二维码扫描控件
        super.onDestroy()
    }
}