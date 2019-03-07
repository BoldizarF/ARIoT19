package com.aptiv.watchdogapp.data.health

import com.aptiv.watchdogapp.data.health.local.HealthCacheDataStore
import com.aptiv.watchdogapp.data.health.local.HeartRateEntity
import com.aptiv.watchdogapp.data.health.remote.HealthRemoteDataStore

class HealthRepository

    constructor(
        private val remoteDataStore: HealthRemoteDataStore,
        private val cacheDataStore: HealthCacheDataStore
    ) {

    suspend fun getValues(): List<HeartRateValue> {
        val remoteValues = remoteDataStore.getRecentHeartRates().entries.map {
            HeartRateEntity(it.key, it.value)
        }

        cacheDataStore.addHeartRateValues(remoteValues)

        return cacheDataStore.getAllHeartRateValues()
            .map { HeartRateValue(it.value, it.timestamp) }
            .sortedBy { it.timestamp }
    }
}
