package com.atmk.iot.bz_statistics.mvvm

/**
 * @author 杨剑
 * @fileName
 * @date 2022-09-14
 * @describe
 * @changeUser
 * @changTime
 */
data class UserInfo(
    val admin: Boolean,
    val chapterTops: List<Any>,
    val coinCount: Int,
    val collectIds: List<Any>,
    val email: String,
    val icon: String,
    val id: Int,
    val nickname: String,
    val password: String,
    val publicName: String,
    val token: String,
    val type: Int,
    val username: String
)
