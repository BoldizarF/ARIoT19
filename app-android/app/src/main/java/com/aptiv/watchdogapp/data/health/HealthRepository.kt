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

    suspend fun getValues(): List<HealthValue> {
        val remoteValues = remoteDataStore.getRecentHealthValues().map { response ->
            val healthValues = response.value.split(":")
            if (healthValues.size == 2) {
                HealthValueEntity(response.key, healthValues[0].toInt(), healthValues[1].toDouble())
            } else {
                null
            }
        }

        cacheDataStore.addHealthValues(remoteValues)

        return cacheDataStore.getAllHeartRateValues()
            .map { HealthValue(it.heartRate, it.temperature, it.timestamp) }
            .sortedBy { it.timestamp }
    }
}
