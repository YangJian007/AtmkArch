package com.atmk.iot.bz_statistics.mvvm

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.io.IOException


/**
 * @author 杨剑
 * @fileName
 * @date 2022-09-13
 * @describe
 * @changeUser
 * @changTime
 */
//https://www.wanandroid.com/user/login

data class BaseReq<T>(
    val data: T?,
    val errorCode: Int,
    val errorMsg: String
)

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


val userServiceApi: UserServiceApi by lazy {

    val retrofit = Retrofit.Builder()
        .client(
            OkHttpClient.Builder()
//            .addInterceptor {
//                it.proceed(it.request()).apply {
//                    Log.i("TAG", "request:${code()} ")
//                    Log.i("TAG", "request:${toString()} ")
//                }
//            }
                .addInterceptor(LoggingInterceptor())
                .build()
        )
        .baseUrl("https://www.wanandroid.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    retrofit.create(UserServiceApi::class.java)

}

/** 日志拦截器 */
class LoggingInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val t1 = System.nanoTime()

        //logV(TAG, "发送请求: ${request.method()} ${request.url()} ${request.headers()}")

        Log.i(
            TAG, String.format(
                "发送请求 %s on %s%n%s",
                request.url, request.method, request.headers
            )
        )

        val response = chain.proceed(request)

        val t2 = System.nanoTime()
        //logV(TAG, "Received response for  ${response.request().url()} in ${(t2 - t1) / 1e6} ms\n${response.headers()}")
        val responseBody: ResponseBody = response.peekBody((1024 * 1024).toLong())
        Log.i(
            TAG,
            String.format(
                "接收响应: [%s] %n返回json:【%s】 %.1fms%n%s",
                response.request.url,
                responseBody.string(),
                (t2 - t1) / 1e6,
                response.headers
            )
        )

        return response
    }

    companion object {
        const val TAG = "LoggingInterceptor"
    }
}

interface UserServiceApi {

    @POST("user/login")
    @FormUrlEncoded
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<BaseReq<UserInfo?>>

    @POST("user/login")
    @FormUrlEncoded
    suspend fun login1(
        @Field("username") username: String,
        @Field("password") password: String
    ): BaseReq<UserInfo?>


}



