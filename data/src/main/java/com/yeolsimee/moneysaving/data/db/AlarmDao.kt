package com.yeolsimee.moneysaving.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yeolsimee.moneysaving.data.entity.AlarmEntity

@Dao
interface AlarmDao {

    @Query("SELECT * FROM routine_alarm")
    fun getAll(): List<AlarmEntity>

    @Insert
    fun insertAll(vararg  alarmEntity: AlarmEntity)

    @Query("DELETE FROM routine_alarm")
    fun deleteAll()

    @Query("DELETE FROM routine_alarm WHERE alarm_id = :alarmId")
    fun delete(alarmId: Int)
}