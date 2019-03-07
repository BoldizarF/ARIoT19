package com.aptiv.watchdogapp.image

class ImageCacheDataStore {

    private val tempList = mutableListOf<CapturedImage>()

    fun getAllHeartRateValues(): List<CapturedImage> {
        return tempList
    }

    fun addHeartRateValues(values: List<CapturedImage>) {
        tempList.addAll(values)
    }
}
