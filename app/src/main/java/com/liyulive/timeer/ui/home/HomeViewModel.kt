package com.liyulive.timeer.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.liyulive.timeer.logic.Repository
import com.liyulive.timeer.logic.model.Timer

class HomeViewModel : ViewModel() {

    private val searchLiveData = MutableLiveData<String>()

    val timeList = ArrayList<Timer>()
    var todayTimeList = ArrayList<Timer>()
    var lastTime: Long = 0
    var today: String = ""

    val timeLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchTimeList(query)
    }

    fun getTimeList(date: String) {
        searchLiveData.value = date
    }
}