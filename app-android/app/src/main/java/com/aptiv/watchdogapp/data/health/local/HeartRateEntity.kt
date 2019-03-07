package com.aptiv.watchdogapp.data.health.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "heartrate_items")
class HeartRateEntity(
    val value: Int,
    val timestamp: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
