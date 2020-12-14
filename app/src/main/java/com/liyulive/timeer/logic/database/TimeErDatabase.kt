package com.liyulive.timeer.logic.database

import com.liyulive.timeer.TimeErApplication
import com.liyulive.timeer.logic.TimerDB
import com.liyulive.timeer.logic.model.Timer

object TimeErDatabase {

    private val timerDao = TimerDB.getDatabase(TimeErApplication.context).timerDao()

     fun insertTimer(timer: Timer) = timerDao.insertTimer(timer)

     fun updateTimer(newTimer: Timer) = timerDao.updateTimer(newTimer)

     fun loadAllTimer() = timerDao.loadAllTimer()

     fun loadTimerByDate(date: String) = timerDao.loadTimerByDate(date)

}