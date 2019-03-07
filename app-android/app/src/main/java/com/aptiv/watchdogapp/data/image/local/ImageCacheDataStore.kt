package com.aptiv.watchdogapp.data.image.local

import com.aptiv.watchdogapp.data.WatchDogDatabase

class ImageCacheDataStore

    constructor(
        private val database: WatchDogDatabase
    ) {

    fun getAllCachedImages(): List<ImageEntity> {
        return database.imageDao().selectAll()
    }

    fun addImages(values: List<ImageEntity>) {
        database.runInTransaction {
            values.forEach {
                database.imageDao().insert(it)
            }
        }
    }

    fun deleteImage(timestamp: Long): Boolean {
        return database.imageDao().delete(timestamp) > 0
    }
}
