package com.aptiv.watchdogapp.data.health.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HealthDao {

    @Query("SELECT * FROM health_value_items")
    fun selectAll(): List<HealthValueEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: HealthValueEntity)

    @Query("DELETE FROM health_value_items")
    fun deleteAll(): Int

}
