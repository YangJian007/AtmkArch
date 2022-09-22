package com.atmk.iot.bz_statistics.room.m

import kotlinx.coroutines.flow.Flow

/**
 * @author        YJ
 * @date          2022-09-20
 * @description
 */
class RoomRespository {

    suspend fun insert(person: Person) {
        AppDatabase.db.personDao.insert(person)
    }

    suspend fun delete(person: Person) {
        AppDatabase.db.personDao.delete(person)
    }

    suspend fun update(person: Person) {
        AppDatabase.db.personDao.update(person)
    }

    fun query(): Flow<List<Person>> {
        return AppDatabase.db.personDao.queryAllPerson()
    }

    suspend fun checkExist(name: String): Boolean {
        return AppDatabase.db.personDao.queryOne(name) == null
    }

}