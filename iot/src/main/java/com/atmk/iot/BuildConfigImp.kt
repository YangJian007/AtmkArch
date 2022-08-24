package com.atmk.iot

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.atmk.base.arouter.BuildConfigService

/**
 * @author 杨剑
 * @fileName
 * @date 2022-08-24
 * @describe
 * @changeUser
 * @changTime
 */

@Route(path = "/iot/buildConfig")
class BuildConfigImp : BuildConfigService {

    var mContext: Context? = null

    override fun init(context: Context?) {
        this.mContext = context
    }

    override fun isDebug(): Boolean {
        return BuildConfig.DEBUG
    }

    override fun isLogEnable(): Boolean {
        return BuildConfig.LOG_ENABLE
    }

    override fun getHostUrl(): String {
        return BuildConfig.HOST_URL
    }

}