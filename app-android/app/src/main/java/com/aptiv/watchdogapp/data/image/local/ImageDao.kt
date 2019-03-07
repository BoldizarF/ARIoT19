package com.aptiv.watchdogapp.data.image.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ImageDao {

    @Query("SELECT * FROM image_items")
    fun selectAll(): List<ImageEntity>

    @Insert
    fun insert(entity: ImageEntity)

}
