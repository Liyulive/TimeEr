package com.liyulive.timeer.logic.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Timer(
    var date: String,
    var startTime: Long,
    var endTime: Long,
    var type: Int,
    var context: String,
    var haveTime: Long
) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

}
