package com.liyulive.timeer.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.liyulive.timeer.TimeErApplication
import com.liyulive.timeer.logic.dao.TypeDao
import com.liyulive.timeer.logic.database.TimeErDatabase
import com.liyulive.timeer.logic.model.DiyType
import com.liyulive.timeer.logic.model.Timer
import kotlinx.coroutines.Dispatchers
import kotlin.concurrent.thread

object Repository {

    val typeDao = TimerDB.getDatabase(TimeErApplication.context).timerDao()

    fun queryAllTime(): List<Timer> {
        return typeDao.loadAllTimer()
    }

    fun queryTimeByDate(date: String): List<Timer> {
        return typeDao.loadTimerByDate(date)
    }

    fun searchTimeList(query: String)= liveData(Dispatchers.IO) {
        val result = queryTimeByDate(query)
        emit(result)
    }

    fun addType(type: DiyType): Long {
        val id = typeDao.insertType(type)
        return id
    }

    fun updateType(type: DiyType) {
        typeDao.updateType(type)
    }

    fun queryAllType(): List<DiyType> {
        val typeList = typeDao.queryType()
        return typeList
    }

    fun deleteType(type: DiyType) {
        typeDao.deleteType(type)
    }

    fun updateTimeItem(time: Timer) {
        typeDao.updateTimer(time)
    }

    fun queryTimerFromType(type: Int): List<Timer> {
        return typeDao.queryFromType(type)
    }

    fun deleteAllType() {
        typeDao.deleteAllType()
    }

    fun deleteAllTimer() {
        typeDao.deleteAllTimer()
    }

}