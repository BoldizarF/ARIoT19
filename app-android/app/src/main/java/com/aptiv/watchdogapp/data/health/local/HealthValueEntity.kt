package com.aptiv.watchdogapp.data.health.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_value_items")
class HealthValueEntity(
    @PrimaryKey
    val timestamp: Long,
    val heartRate: Int,
    val temperature: Double
)
