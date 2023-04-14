package com.yeolsimee.moneysaving.data

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.yeolsimee.moneysaving.data.api.RoutineApiService
import com.yeolsimee.moneysaving.data.api.UserApiService
import com.yeolsimee.moneysaving.data.interceptor.ConnectivityInterceptor
import com.yeolsimee.moneysaving.data.interceptor.FirebaseUserIdTokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetModule {
    @Provides
    @Singleton
    fun provideHttpCache(application: Application): Cache {
        val cacheSize = 10L * 1024 * 1024
        return Cache(application.cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .create()
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(
        connectivityInterceptor: ConnectivityInterceptor,
        authInterceptor: FirebaseUserIdTokenInterceptor,
        cache: Cache? = null
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(connectivityInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    internal fun interceptor(): ConnectivityInterceptor = ConnectivityInterceptor()

    @Provides
    @Singleton
    internal fun authInterceptor(): FirebaseUserIdTokenInterceptor =
        FirebaseUserIdTokenInterceptor()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRoutineApiService(retrofit: Retrofit): RoutineApiService {
        return retrofit.create(RoutineApiService::class.java)
    }

    companion object {
        private const val BASE_URL: String = BuildConfig.MOCK_URL
    }
}