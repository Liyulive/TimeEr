package com.liyulive.timeer.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.liyulive.timeer.logic.Repository
import com.liyulive.timeer.logic.model.DiyType
import com.liyulive.timeer.logic.model.Timer

class HomeViewModel : ViewModel() {

    private val searchLiveData = MutableLiveData<String>()
    val typeListLiveData = MutableLiveData<List<DiyType>>()
    val forAdapterLiveData = MutableLiveData<List<Timer>>()

    var timeList = ArrayList<Timer>()
    var typeList = ArrayList<DiyType>()
    var timeListForAdapter = ArrayList<Timer>()
    var lastTime: Long = 0
    var today: String = ""
    var selectDay: String = ""

    init {
        typeListLiveData.value = typeList
        forAdapterLiveData.value = timeListForAdapter
    }

    val timeLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchTimeList(query)
    }

    fun getTimeList(date: String) {
        searchLiveData.value = date
    }
}