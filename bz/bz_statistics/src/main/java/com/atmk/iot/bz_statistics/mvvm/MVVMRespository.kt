package com.atmk.iot.bz_statistics.mvvm

/**
 * @author 杨剑
 * @fileName
 * @date 2022-09-13
 * @describe
 * @changeUser
 * @changTime
 */
class MVVMRespository {

    suspend fun getUser(name:String,password:String):UserInfo?{
        return userServiceApi.login1(name,password).data
    }

}