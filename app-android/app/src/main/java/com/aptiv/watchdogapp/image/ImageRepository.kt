package com.aptiv.watchdogapp.image


class ImageRepository
    constructor(
        private val remoteDataStore: ImageRemoteDataStore,
        private val cacheDataStore: ImageCacheDataStore
    ) {

    suspend fun getValues(): List<CapturedImage> {
        val remoteValues = remoteDataStore.getRecentHeartRates().entries.map {
            CapturedImage(it.value, it.key)
        }

        cacheDataStore.addHeartRateValues(remoteValues)

        return cacheDataStore.getAllHeartRateValues().sortedBy { it.timestamp }
    }
}
