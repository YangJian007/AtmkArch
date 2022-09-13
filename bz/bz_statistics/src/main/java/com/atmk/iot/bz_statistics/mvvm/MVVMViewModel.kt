package com.atmk.iot.bz_statistics.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

/**
 * @author 杨剑
 * @fileName
 * @date 2022-09-13
 * @describe
 * @changeUser
 * @changTime
 */
class MVVMViewModel : ViewModel() {


    fun getUserInfo(username:String,password:String)= flow {
        val userInfo = MVVMRespository().getUser(username, password)
        emit(userInfo)
    }.catch {
        if (it is Exception) {
            HttpErrorDeal.dealHttpError(it)
        }
        emit(null)
    }.asLiveData()

}