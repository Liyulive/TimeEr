package com.liyulive.timeer.logic.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DiyType(var typeName: String, var typeContext: String, var typeColor: Int) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

}