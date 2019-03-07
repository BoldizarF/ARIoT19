package com.aptiv.watchdogapp.health

class HealthCacheDataStore {

    private val tempList = mutableListOf<HeartRateValue>()

    fun getAllHeartRateValues(): List<HeartRateValue> {
        return tempList
    }

    fun addHeartRateValues(values: List<HeartRateValue>) {
        tempList.addAll(values)
    }
}
