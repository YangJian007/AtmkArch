package com.atmk.iot.bz_statistics.room.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atmk.iot.bz_statistics.room.m.Person
import com.atmk.iot.bz_statistics.room.m.RoomRespository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

/**
 * @author        YJ
 * @date          2022-09-20
 * @description
 */

class RoomViewModel : ViewModel() {

    val respository: RoomRespository by lazy { RoomRespository() }

    fun insert(name: String, age: Int) {
        viewModelScope.launch(Dispatchers.IO) {
//            if (respository.checkExist(name))
                respository.insert(Person(name, age))
        }
    }

    fun delete(name: String, age: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            respository.delete(Person(name, age))
        }
    }

    fun update(name: String, age: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            respository.update(Person(name, age))
        }
    }

    fun query() = respository.query().flowOn(Dispatchers.IO)

}