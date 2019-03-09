package com.aptiv.watchdogapp.data.health.remote

import com.aptiv.watchdogapp.data.WatchDogApiService

class HealthRemoteDataStore

    constructor(
        private val api: WatchDogApiService
    ) {

    suspend fun getRecentHealthValues(apiKey: String): Map<Long, String> {
        return api.getLatestHealthValues(apiKey).await()
    }

}
