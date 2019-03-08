package com.aptiv.watchdogapp.data.health.local

import com.aptiv.watchdogapp.data.WatchDogDatabase

class HealthCacheDataStore

    constructor(
        private val database: WatchDogDatabase
    ) {

    fun clearAll(): Boolean {
        return database.healthDao().deleteAll() > 0
    }

    fun getAllHeartRateValues(): List<HealthValueEntity> {
        return database.healthDao().selectAll()
    }

    fun addHealthValues(values: List<HealthValueEntity?>) {
        database.runInTransaction {
            values.forEach {
                if (it != null && isValidEntity(it)) {
                    database.healthDao().insert(it)
                }
            }
        }
    }

    private fun isValidEntity(entity: HealthValueEntity): Boolean {
        return entity.heartRate in 30..220 && entity.temperature in 10.0..70.0
    }
}
