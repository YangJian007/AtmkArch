package com.atmk.iot

import com.atmk.base.BzBaseModuleInit
import com.hjq.base.application.ApplicationProvider
import com.hjq.base.application.ModuleInitDelegate
import dagger.hilt.android.HiltAndroidApp

/**
 * @author 杨剑
 * @fileName
 * @date 2022-08-24
 * @describe
 * @changeUser
 * @changTime
 */
@HiltAndroidApp
class IotApplication : ApplicationProvider() {

    init {
        ModuleInitDelegate.register(BzBaseModuleInit())
    }

}