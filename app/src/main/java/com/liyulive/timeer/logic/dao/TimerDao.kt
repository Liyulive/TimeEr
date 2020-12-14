package com.liyulive.timeer.logic.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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

}