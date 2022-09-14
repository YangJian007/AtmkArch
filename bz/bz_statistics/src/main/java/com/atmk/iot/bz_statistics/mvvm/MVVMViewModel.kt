package com.atmk.iot.bz_statistics.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.atmk.base.http.retrofit.HttpErrorDeal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * @author 杨剑
 * @fileName
 * @date 2022-09-13
 * @describe
 * @changeUser
 * @changTime
 */

@HiltViewModel
class MVVMViewModel @Inject constructor(private val mvvmRespository: MVVMRespository) : ViewModel() {


    fun getUserInfo(username:String,password:String)= flow {
        val userInfo = mvvmRespository.getUser(username, password)
        emit(userInfo)
    }.catch {
        if (it is Exception) {
            HttpErrorDeal.dealHttpError(it)
        }
        emit(null)
    }.asLiveData()

}