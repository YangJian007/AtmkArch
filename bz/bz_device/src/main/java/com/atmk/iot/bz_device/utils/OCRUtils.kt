package com.atmk.iot.bz_device.utils

import android.content.Context
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.GeneralParams
import com.baidu.ocr.sdk.model.GeneralResult
import java.io.File
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author 杨剑
 * @fileName
 * @date 2022-08-17
 * @describe
 * @changeUser
 * @changTime
 */
object OCRUtils {

    fun getSaveFile(context: Context): File {
        return File(context.filesDir, "pic.jpg")
    }


    interface ServiceListener {
        fun onResult(result: List<String>)
    }


    fun recAccurateBasic(
        ctx: Context?,
        filePath: String?,
        listener: ServiceListener
    ) {
        val param = GeneralParams()
        param.setDetectDirection(true)
        param.setVertexesLocation(true)
        param.setRecognizeGranularity(GeneralParams.GRANULARITY_SMALL)
        param.imageFile = File(filePath)
        OCR.getInstance(ctx)
            .recognizeAccurateBasic(param, object : OnResultListener<GeneralResult> {
                override fun onResult(result: GeneralResult) {
                    val list = result.wordList.map {
                        it.words
                    }.filter {
                        val pattern = "^[A-Za-z0-9]+$"
                        val r: Pattern = Pattern.compile(pattern)
                        val m: Matcher = r.matcher(it)
                        m.matches()
                    }.filter {
                        it.length > 6
                    }
                    listener.onResult(list)
                }

                override fun onError(error: OCRError) {
                    listener.onResult(mutableListOf())
                }
            })
    }
}