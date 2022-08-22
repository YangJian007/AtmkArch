package com.atmk.iot.bz_login.api

import com.hjq.http.config.IRequestApi
import com.hjq.http.config.IRequestType
import com.hjq.http.model.BodyType
import java.net.URLEncoder

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2019/12/07
 *    desc   : 用户登录
 */
class LoginApi : IRequestApi, IRequestType {

    override fun getApi() = "authorize/login"

    override fun getType() = BodyType.JSON


    var username: String? = null
    var password: String? = null
    var verifyCode: String? = null
    var tokenType: String? = "default"
    var verifyKey: String? = null


    data class Bean(
        val currentAuthority: List<String>,
        val expires: Int,
        val permissions: List<Permission>,
        val roles: List<Role>,
        val token: String,
        val user: User,
        val userId: String
    )

    data class Permission(
        val actions: List<String>,
        val dataAccesses: List<Any>,
        val id: String,
        val name: String,
        val options: Options
    )

    data class Role(
        val id: String,
        val name: String,
        val options: OptionsX,
        val type: String
    )

    data class User(
        val createTime: Long,
        val id: String,
        val name: String,
        val tenantDisabled: Boolean,
        val tenants: List<Tenant>,
        val username: String
    )

    data class Options(
        val type: List<String>
    )

    data class OptionsX(
        val admin: Boolean,
        val allDataAccess: Boolean,
        val main: Boolean,
        val memberId: String,
        val memberName: String,
        val memberType: String
    )

    data class Tenant(
        val adminMember: Boolean,
        val mainTenant: Boolean,
        val state: State,
        val tenantId: String,
        val tenantName: String,
        val tenantState: TenantState,
        val type: String,
        val userId: String
    )

    data class State(
        val text: String,
        val value: String
    )

    data class TenantState(
        val text: String,
        val value: String
    )


}