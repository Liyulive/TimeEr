package com.liyulive.timeer.logic

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.liyulive.timeer.logic.dao.TimerDao
import com.liyulive.timeer.logic.dao.TypeDao
import com.liyulive.timeer.logic.model.DiyType
import com.liyulive.timeer.logic.model.Timer
import java.lang.reflect.Executable
import java.util.concurrent.Executors

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
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Executors.newSingleThreadExecutor().execute {
                            instance?.typeDao()?.insertType(DiyType("学习", "上课/听讲/思考/研究/实践", 1))
                            instance?.typeDao()?.insertType(DiyType("歇息", "小憩一会儿", 2))
                            instance?.typeDao()?.insertType(DiyType("休闲", "游戏/音乐/阅读", 3))
                            instance?.typeDao()?.insertType(DiyType("运动", "各种体育运动", 4))
                            instance?.typeDao()?.insertType(DiyType("睡觉", "睡觉/午睡", 5))
                            instance?.typeDao()?.insertType(DiyType("用餐", "干饭干饭~", 6))
                            instance?.typeDao()?.insertType(DiyType("杂项", "杂七杂八一些事情囖", 7))
                            Log.d("test", "db")
                        }
                    }
                })
                .build().apply {
                    instance = this
                }
        }
    }

}