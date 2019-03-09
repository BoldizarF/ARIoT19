package com.aptiv.watchdogapp.data.image

import com.aptiv.watchdogapp.data.image.local.ImageCacheDataStore
import com.aptiv.watchdogapp.data.image.local.ImageEntity
import com.aptiv.watchdogapp.data.image.remote.ImageRemoteDataStore


class ImageRepository
    constructor(
        private val remoteDataStore: ImageRemoteDataStore,
        private val cacheDataStore: ImageCacheDataStore
    ) {

    fun deleteAll(): Boolean {
        return cacheDataStore.deleteAll()
    }

    fun deleteImage(timestamp: Long): Boolean {
        return cacheDataStore.deleteImage(timestamp)
    }

    suspend fun getValues(apiKey: String): List<CapturedImage> {
        val remoteValues = remoteDataStore.getRecentHeartRates(apiKey).entries.map {
            val timestamp = it.key * 1000L
            ImageEntity(it.value, timestamp)
        }

        cacheDataStore.addImages(remoteValues)

        return cacheDataStore.getAllCachedImages()
            .map { CapturedImage(it.value, it.timestamp) }
            .sortedByDescending { it.timestamp }
    }
}
