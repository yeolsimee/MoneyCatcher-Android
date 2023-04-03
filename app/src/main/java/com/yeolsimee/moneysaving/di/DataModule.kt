package com.yeolsimee.moneysaving.di

import com.yeolsimee.moneysaving.data.api.TestApiRepository
import com.yeolsimee.moneysaving.data.api.TestApiService
import com.yeolsimee.moneysaving.data.sample.SampleSource
import com.yeolsimee.moneysaving.domain.repository.ITestApiRepository
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
    fun provideTestApiRepository(source: SampleSource): ITestApiRepository = TestApiRepository(source)

    @Provides
    @Singleton
    fun provideSampleSource(api: TestApiService): SampleSource = SampleSource(api)
}