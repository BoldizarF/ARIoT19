package com.aptiv.watchdogapp.data.health

data class HealthValue(
    val heartRate: Int,
    val temperature: Double,
    val timestamp: Long
)
