package com.liyulive.timeer.logic.dao

import androidx.room.*
import com.liyulive.timeer.logic.model.DiyType

@Dao
interface TypeDao {

    @Insert
    fun insertType(type: DiyType): Long

    @Update
    fun updateType(newType: DiyType)

    @Query("select * from DiyType")
    fun queryType(): List<DiyType>

    @Delete
    fun deleteType(type: DiyType)


}