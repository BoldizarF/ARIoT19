package com.aptiv.watchdogapp.data.health.remote

import com.aptiv.watchdogapp.data.WatchDogApiService

class HealthRemoteDataStore

    constructor(
        private val api: WatchDogApiService
    ) {

    suspend fun getRecentHeartRates(): Map<Long, Int> {
        return api.getLatestHeartRateValues().await()
    }

}
