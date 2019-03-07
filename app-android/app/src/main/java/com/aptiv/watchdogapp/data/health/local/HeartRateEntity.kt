package com.aptiv.watchdogapp.data.health.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "heartrate_items")
class HeartRateEntity(
    @PrimaryKey
    val timestamp: Long,
    val value: Int
)
