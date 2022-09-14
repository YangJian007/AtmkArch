package com.atmk.iot.bz_statistics.netdi

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import retrofit2.Retrofit

/**
 * @author 杨剑
 * @fileName
 * @date 2022-09-14
 * @describe
 * @changeUser
 * @changTime
 */

@Module
@InstallIn(ActivityRetainedComponent::class)
object ApiServiceModule {


    @Provides
    fun provideUserService(retrofit: Retrofit): UserServiceApi =
        retrofit.create(UserServiceApi::class.java)

}