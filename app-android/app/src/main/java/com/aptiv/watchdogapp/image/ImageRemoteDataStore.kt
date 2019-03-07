package com.aptiv.watchdogapp.image

import com.aptiv.watchdogapp.WatchDogApiService

class ImageRemoteDataStore

    constructor(
        private val api: WatchDogApiService
    ) {

    suspend fun getRecentHeartRates(): Map<Long, String> {
        return api.getLatestImages().await()
    }

}
