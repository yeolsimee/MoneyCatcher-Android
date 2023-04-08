package com.yeolsimee.moneysaving.di

import com.yeolsimee.moneysaving.domain.repository.IUserApiRepository
import com.yeolsimee.moneysaving.domain.usecase.UserUseCase
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
    fun provideLoginResult(repository: IUserApiRepository): UserUseCase = UserUseCase(repository)
}