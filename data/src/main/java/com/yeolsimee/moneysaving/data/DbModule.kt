package com.yeolsimee.moneysaving.data

import android.app.Application
import androidx.room.Room
import com.yeolsimee.moneysaving.data.db.AlarmDao
import com.yeolsimee.moneysaving.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbModule {

    @Provides
    @Singleton
    internal fun provideDb(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java, "roumo"
        ).build()
    }


    @Provides
    @Singleton
    fun provideAlarmDao(db: AppDatabase): AlarmDao {
        return db.alarmDao()
    }
}