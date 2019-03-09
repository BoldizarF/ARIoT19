package com.aptiv.watchdogapp.data.image.remote

import com.aptiv.watchdogapp.data.WatchDogApiService

class ImageRemoteDataStore

    constructor(
        private val api: WatchDogApiService
    ) {

    suspend fun getRecentHeartRates(apiKey: String): Map<Long, String> {
        return api.getLatestImages(apiKey).await()
    }

}
