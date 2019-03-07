package com.aptiv.watchdogapp.data.image.local

import com.aptiv.watchdogapp.data.WatchDogDatabase

class ImageCacheDataStore

    constructor(
        private val database: WatchDogDatabase
    ) {

    fun getAllHeartRateValues(): List<ImageEntity> {
        return database.imageDao().selectAll()
    }

    fun addHeartRateValues(values: List<ImageEntity>) {
        database.runInTransaction {
            values.forEach {
                database.imageDao().insert(it)
            }
        }
    }
}
