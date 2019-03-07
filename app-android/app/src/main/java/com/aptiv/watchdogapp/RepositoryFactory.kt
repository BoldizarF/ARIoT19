package com.aptiv.watchdogapp

import android.content.Context
import com.aptiv.watchdogapp.health.HealthCacheDataStore
import com.aptiv.watchdogapp.health.HealthRemoteDataStore
import com.aptiv.watchdogapp.health.HealthRepository
import com.aptiv.watchdogapp.image.ImageCacheDataStore
import com.aptiv.watchdogapp.image.ImageRemoteDataStore
import com.aptiv.watchdogapp.image.ImageRepository

object RepositoryFactory {

    fun createHealthRepository(context: Context): HealthRepository {
        val api = RetrofitFactory.create()
        return HealthRepository(
            HealthRemoteDataStore(api),
            HealthCacheDataStore()
        )
    }

    fun createImagesRepository(context: Context): ImageRepository {
        val api = RetrofitFactory.create()
        return ImageRepository(
            ImageRemoteDataStore(api),
            ImageCacheDataStore()
        )
    }
}
