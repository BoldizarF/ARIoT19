package com.aptiv.watchdogapp.health

import com.aptiv.watchdogapp.WatchDogApiService

class HealthRemoteDataStore

    constructor(
        private val api: WatchDogApiService
    ) {

    suspend fun getRecentHeartRates(): Map<Long, Int> {
        return api.getLatestHeartRateValues().await()
    }

}
