package com.atmk.base.http.retrofit

import android.os.Build
import android.util.Log
import com.atmk.base.other.AppConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
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
//    private  val BASE_URL = AppConfig.getHostUrl()

    /** 提供OkHttpClient */
    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(TokenInterceptor())
            .addInterceptor(HttpLoggingInterceptor { Log.i(AppConfig.getHttpTag(), it) }.apply { level = HttpLoggingInterceptor.Level.BODY })
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

/*    */
    /** 提供服务 *//*
    @Singleton
    @Provides
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)*/


    /** 增加Header */
    class HeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val request = original.newBuilder().apply {
                header("X-Access-Token", System.currentTimeMillis().toString())
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