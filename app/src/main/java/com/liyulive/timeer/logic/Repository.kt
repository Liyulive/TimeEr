package com.liyulive.timeer.logic

import androidx.lifecycle.liveData
import com.liyulive.timeer.logic.database.TimeErDatabase
import kotlinx.coroutines.Dispatchers

object Repository {

    fun searchTimeList(query: String)= liveData(Dispatchers.IO) {
        val result = TimeErDatabase.loadTimerByDate(query)
        emit(result)
    }

}