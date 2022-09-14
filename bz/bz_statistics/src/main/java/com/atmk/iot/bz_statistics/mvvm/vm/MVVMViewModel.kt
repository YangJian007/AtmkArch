package com.atmk.iot.bz_statistics.mvvm.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atmk.base.http.retrofit.HttpErrorDeal
import com.atmk.iot.bz_statistics.mvvm.m.MVVMRespository
import com.atmk.iot.bz_statistics.mvvm.m.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
class MVVMViewModel @Inject constructor(private val mvvmRespository: MVVMRespository) :
    ViewModel() {

    val userInfo = MutableStateFlow<UserInfo?>(null)

    fun getUserInfo(username: String, password: String) {

        viewModelScope.launch {
            flow {
                val userInfo = mvvmRespository.getUser(username, password)
                emit(userInfo)
            }.flowOn(Dispatchers.IO)
                .catch {
                    if (it is Exception) {
                        HttpErrorDeal.dealHttpError(it)
                    }
                    emit(null)
                }.collect {
                    userInfo.value = it
                }
        }

    }


}