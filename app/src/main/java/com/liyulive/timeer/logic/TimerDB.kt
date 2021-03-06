package com.liyulive.timeer.logic

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.liyulive.timeer.logic.dao.TimerDao
import com.liyulive.timeer.logic.dao.TypeDao
import com.liyulive.timeer.logic.model.DiyType
import com.liyulive.timeer.logic.model.Timer

@Database(version = 1, entities = [Timer::class, DiyType::class])
abstract class TimerDB : RoomDatabase() {

    abstract fun timerDao(): TimerDao
    abstract fun typeDao(): TypeDao

    companion object {

        private var instance: TimerDB? = null

        @Synchronized
        fun getDatabase(context: Context): TimerDB {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext, TimerDB::class.java, "TimeErDB")
                    .allowMainThreadQueries()
                    .build().apply {
                        instance = this
                    }
        }
    }

}