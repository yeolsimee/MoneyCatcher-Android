package com.yeolsimee.moneysaving.di

import com.yeolsimee.moneysaving.data.api.CategoryApiService
import com.yeolsimee.moneysaving.data.api.RoutineApiService
import com.yeolsimee.moneysaving.data.api.UserApiService
import com.yeolsimee.moneysaving.data.repository.CategoryApiRepository
import com.yeolsimee.moneysaving.data.repository.RoutineApiRepository
import com.yeolsimee.moneysaving.data.repository.SettingsRepository
import com.yeolsimee.moneysaving.data.repository.UserApiRepository
import com.yeolsimee.moneysaving.data.source.RoutineSource
import com.yeolsimee.moneysaving.data.source.SettingsSource
import com.yeolsimee.moneysaving.data.source.UserSource
import com.yeolsimee.moneysaving.domain.repository.ICategoryApiRepository
import com.yeolsimee.moneysaving.domain.repository.IRoutineApiRepository
import com.yeolsimee.moneysaving.domain.repository.IUserApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideUserApiRepository(source: UserSource): IUserApiRepository = UserApiRepository(source)

    @Provides
    @Singleton
    fun provideUserSource(api: UserApiService): UserSource = UserSource(api)

    @Provides
    @Singleton
    fun provideRoutineApiRepository(source: RoutineSource): IRoutineApiRepository = RoutineApiRepository(source)

    @Provides
    @Singleton
    fun provideRoutineSource(api: RoutineApiService): RoutineSource = RoutineSource(api)

    @Provides
    @Singleton
    fun provideCategoryApiRepository(api: CategoryApiService): ICategoryApiRepository = CategoryApiRepository(api)

    @Provides
    @Singleton
    fun provideSettingsRepository(source: SettingsSource) = SettingsRepository(source)

    @Provides
    @Singleton
    fun provideSettingsSource() = SettingsSource()

//    @Provides
//    @Singleton
//    fun provideSettingsSource() = SettingsSource()
}