package com.aptiv.watchdogapp.data.health

import com.aptiv.watchdogapp.data.health.local.HealthCacheDataStore
import com.aptiv.watchdogapp.data.health.local.HealthValueEntity
import com.aptiv.watchdogapp.data.health.remote.HealthRemoteDataStore

class HealthRepository

    constructor(
        private val remoteDataStore: HealthRemoteDataStore,
        private val cacheDataStore: HealthCacheDataStore
    ) {

    fun clearAll(): Boolean {
        return cacheDataStore.clearAll()
    }

    suspend fun getValues(apiKey: String): List<HealthValue> {
        val remoteValues = remoteDataStore.getRecentHealthValues(apiKey).map { response ->
            val healthValues = response.value.split(":")
            if (healthValues.size == 2) {
                val timestamp = response.key * 1000L
                HealthValueEntity(timestamp, healthValues[0].toInt(), healthValues[1].toDouble())
            } else {
                null
            }
        }

        cacheDataStore.addHealthValues(remoteValues)

        return cacheDataStore.getAllHealthValues()
            .map { HealthValue(it.heartRate, it.temperature, it.timestamp) }
            .sortedBy { it.timestamp }
    }
}
