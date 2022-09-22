package com.atmk.iot.bz_statistics.room.m

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hjq.base.application.ApplicationProvider

/**
 * @author        YJ
 * @date          2022-09-20
 * @description
 */
@Database(entities = [Person::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract val personDao: PersonDao

    companion object {

        val db: AppDatabase by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Room.databaseBuilder(
                ApplicationProvider.appContext,
                AppDatabase::class.java,
                "test.db"
            ).build()
        }

    }

}