package com.atmk.iot.bz_login

import com.atmk.base.BzBaseModuleInit
import com.hjq.base.application.ApplicationProvider
import com.hjq.base.application.ModuleInitDelegate

/**
 * @author 杨剑
 * @fileName
 * @date 2022-08-24
 * @describe
 * @changeUser
 * @changTime
 */
class LoginApplication : ApplicationProvider() {

    init {
        ModuleInitDelegate.register(BzBaseModuleInit())
    }

}