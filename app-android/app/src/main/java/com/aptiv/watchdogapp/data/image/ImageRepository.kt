package com.aptiv.watchdogapp.data.image

import com.aptiv.watchdogapp.data.image.local.ImageCacheDataStore
import com.aptiv.watchdogapp.data.image.local.ImageEntity
import com.aptiv.watchdogapp.data.image.remote.ImageRemoteDataStore


class ImageRepository
    constructor(
        private val remoteDataStore: ImageRemoteDataStore,
        private val cacheDataStore: ImageCacheDataStore
    ) {

    suspend fun deleteImage(timestamp: Long): Boolean {
        return cacheDataStore.deleteImage(timestamp)
    }

    suspend fun getValues(): List<CapturedImage> {
        val remoteValues = remoteDataStore.getRecentHeartRates().entries.map {
            ImageEntity(it.value, it.key)
        }

        cacheDataStore.addImages(remoteValues)

        return cacheDataStore.getAllCachedImages()
            .map { CapturedImage(it.value, it.timestamp) }
            .sortedBy { it.timestamp }
    }
}
