package com.aptiv.watchdogapp.health

class HealthRepository
    constructor(
        private val remoteDataStore: HealthRemoteDataStore,
        private val cacheDataStore: HealthCacheDataStore
    ) {

    suspend fun getValues(): List<HeartRateValue> {
        val remoteValues = remoteDataStore.getRecentHeartRates().entries.map {
            HeartRateValue(it.value, it.key)
        }

        cacheDataStore.addHeartRateValues(remoteValues)

        return cacheDataStore.getAllHeartRateValues().sortedBy { it.timestamp }
    }
}
