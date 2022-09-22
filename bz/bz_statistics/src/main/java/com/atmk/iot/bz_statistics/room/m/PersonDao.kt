package com.atmk.iot.bz_statistics.room.m

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * @author        YJ
 * @date          2022-09-20
 * @description   Dao-(Data Access Object)
 */
@Dao
interface PersonDao {

    //遇冲突，覆盖
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(person: Person)

    @Delete
    suspend fun delete(person: Person)

    @Update
    suspend fun update(person: Person)

    @Query("select * from Person")
    fun queryAllPerson(): Flow<List<Person>>

    @Query("select * from Person where name == :name")
    suspend fun queryOne(name: String): Person?

}