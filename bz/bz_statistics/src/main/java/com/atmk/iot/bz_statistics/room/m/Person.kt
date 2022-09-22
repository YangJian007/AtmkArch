package com.atmk.iot.bz_statistics.room.m

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author        YJ
 * @date          2022-09-20
 * @description   数据实体
 */
@Entity
data class Person(
    @PrimaryKey val name: String,
    val age: Int
)
