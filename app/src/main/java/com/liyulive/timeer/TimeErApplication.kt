package com.liyulive.timeer

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.liyulive.timeer.logic.model.ArcData

class TimeErApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        var ArcData = ArrayList<ArcData>()
    }

    init {
        ArcData.add(ArcData(250.0, 1, "hello")) //无法理解的bug但是能够运行
        ArcData.clear()
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}