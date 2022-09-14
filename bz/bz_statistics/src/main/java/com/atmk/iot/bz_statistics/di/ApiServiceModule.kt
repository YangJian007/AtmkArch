package com.atmk.iot.bz_statistics.di

import com.atmk.iot.bz_statistics.net.UserServiceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * @author 杨剑
 * @fileName
 * @date 2022-09-14
 * @describe
 * @changeUser
 * @changTime
 */

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {


    @Singleton
    @Provides
    fun provideUserService(retrofit: Retrofit): UserServiceApi =
        retrofit.create(UserServiceApi::class.java)

}