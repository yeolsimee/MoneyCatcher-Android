package com.yeolsimee.moneysaving.domain

import com.yeolsimee.moneysaving.data.api.TestApiService
import com.yeolsimee.moneysaving.domain.usecase.SampleUseCase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    @Provides
    @Singleton
    fun provideSampleData(testApiService: TestApiService): SampleUseCase {
        return SampleUseCase(testApiService)
    }
}