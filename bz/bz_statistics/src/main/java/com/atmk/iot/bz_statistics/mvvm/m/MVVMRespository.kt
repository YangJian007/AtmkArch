package com.atmk.iot.bz_statistics.mvvm.m

import com.atmk.iot.bz_statistics.mvvm.m.UserInfo
import com.atmk.iot.bz_statistics.net.UserServiceApi
import javax.inject.Inject

/**
 * @author 杨剑
 * @fileName
 * @date 2022-09-13
 * @describe
 * @changeUser
 * @changTime
 */
class MVVMRespository @Inject constructor(
    private val api: UserServiceApi
) {

    suspend fun getUser(name: String, password: String): UserInfo? {
        return api.login1(name, password).data
    }

}