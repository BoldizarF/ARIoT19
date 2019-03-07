package com.aptiv.watchdogapp.health

class HealthRepository
    constructor(
        private val remoteDataStore: HealthRemoteDataStore,
        private val cacheDataStore: HealthCacheDataStore
    ) {


    fun getValues(): List<HeartRateValue> {
        val remoteValues = remoteDataStore.getRecentHeartRates().map {
            HeartRateValue(it.second, it.first)
        }

        cacheDataStore.addHeartRateValues(remoteValues)

        return cacheDataStore.getAllHeartRateValues()
    }

}
