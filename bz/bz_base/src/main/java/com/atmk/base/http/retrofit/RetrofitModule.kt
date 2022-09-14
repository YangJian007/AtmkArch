package com.atmk.base.http.retrofit

import android.os.Build
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import javax.inject.Singleton


/**
 * @author 杨剑
 * @fileName
 * @date 2022-09-08
 * @describe
 * @changeUser
 * @changTime
 */
@Module
@InstallIn(SingletonComponent::class)
/** 单例 */
object RetrofitModule {

    /** 服务地址 */
    private const val BASE_URL = "https://www.wanandroid.com/"

    /** 提供OkHttpClient */
    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(LoggingInterceptor())
//            .addInterceptor(HeaderInterceptor())
//            .addInterceptor(BasicParamsInterceptor())
            .build()
    }

    /** 提供Retrofit */
    @Singleton
    @Provides
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        val builder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
//            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())

        return builder.build()
    }

    /** 用hilt就不用这种创建对象的方式了 */
    fun <T> create(serviceClass: Class<T>): T =
        provideRetrofit(provideHttpClient()).create(serviceClass)

/*    *//** 提供服务 *//*
    @Singleton
    @Provides
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)*/


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
                    request.url(), request.method(), request.headers()
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
                    response.request().url(),
                    responseBody.string(),
                    (t2 - t1) / 1e6,
                    response.headers()
                )
            )

            return response
        }

        companion object {
            const val TAG = "LoggingInterceptor"
        }
    }

    /** 增加Header */
    class HeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val request = original.newBuilder().apply {
                header("model", "Android")
                header("If-Modified-Since", "${Date()}")
                header("User-Agent", System.getProperty("http.agent") ?: "unknown")


            }.build()
            return chain.proceed(request)
        }
    }

    /** 公共参数 */
    class BasicParamsInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val originalHttpUrl = originalRequest.url()
            val url = originalHttpUrl.newBuilder().apply {
                addQueryParameter("version", "${Build.VERSION.SDK_INT}")
            }.build()
            val request = originalRequest.newBuilder().url(url).method(
                originalRequest.method(),
                originalRequest.body()
            ).build()
            return chain.proceed(request)
        }
    }
}