package com.yeolsimee.moneysaving.di

import com.yeolsimee.moneysaving.domain.repository.ITestApiRepository
import com.yeolsimee.moneysaving.domain.usecase.SampleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideSampleData(iTestApiRepository: ITestApiRepository): SampleUseCase = SampleUseCase(iTestApiRepository)
}