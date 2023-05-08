package com.yeolsimee.moneysaving.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yeolsimee.moneysaving.data.entity.AlarmEntity

@Database(entities = [AlarmEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
}