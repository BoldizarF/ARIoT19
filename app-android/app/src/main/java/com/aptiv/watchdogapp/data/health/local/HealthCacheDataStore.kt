package com.aptiv.watchdogapp.data.health.local

import com.aptiv.watchdogapp.data.WatchDogDatabase
import com.aptiv.watchdogapp.data.health.HeartRateValue

class HealthCacheDataStore

    constructor(
        private val database: WatchDogDatabase
    ) {

    fun clearAll(): Boolean {
        return database.healthDao().deleteAll() > 0
    }

    fun getAllHeartRateValues(): List<HeartRateEntity> {
        return database.healthDao().selectAll()
    }

    fun addHeartRateValues(values: List<HeartRateEntity>) {
        database.runInTransaction {
            values.forEach {
                database.healthDao().insert(it)
            }
        }
    }
}
