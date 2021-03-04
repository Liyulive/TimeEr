package com.liyulive.timeer.logic.dao

import androidx.room.*
import com.liyulive.timeer.logic.model.DiyType
import com.liyulive.timeer.logic.model.Timer

@Dao
interface TimerDao {

    @Insert
    fun insertTimer(timer: Timer): Long

    @Update
    fun updateTimer(newTimer: Timer)

    @Query("select * from Timer")
    fun loadAllTimer(): List<Timer>

    @Query("select * from Timer where date = :date")
    fun loadTimerByDate(date: String): List<Timer>


    @Insert
    fun insertType(type: DiyType): Long

    @Update
    fun updateType(newType: DiyType)

    @Query("select * from DiyType")
    fun queryType(): List<DiyType>

    @Delete
    fun deleteType(type: DiyType)

}