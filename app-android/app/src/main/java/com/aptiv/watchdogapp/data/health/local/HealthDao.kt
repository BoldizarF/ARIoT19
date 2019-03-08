package com.aptiv.watchdogapp.data.health.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HealthDao {

    @Query("SELECT * FROM heartrate_items")
    fun selectAll(): List<HeartRateEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: HeartRateEntity)

    @Query("DELETE FROM heartrate_items")
    fun deleteAll(): Int

}
