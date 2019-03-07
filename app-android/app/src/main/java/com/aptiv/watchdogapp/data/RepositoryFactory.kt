package com.aptiv.watchdogapp.data

import android.content.Context
import com.aptiv.watchdogapp.data.health.local.HealthCacheDataStore
import com.aptiv.watchdogapp.data.health.remote.HealthRemoteDataStore
import com.aptiv.watchdogapp.data.health.HealthRepository
import com.aptiv.watchdogapp.data.image.local.ImageCacheDataStore
import com.aptiv.watchdogapp.data.image.remote.ImageRemoteDataStore
import com.aptiv.watchdogapp.data.image.ImageRepository

object RepositoryFactory {

    fun createHealthRepository(context: Context): HealthRepository {
        val api = RetrofitFactory.create()
        return HealthRepository(
            HealthRemoteDataStore(api),
            HealthCacheDataStore(WatchDogDatabase.getDatabase(context))
        )
    }

    fun createImagesRepository(context: Context): ImageRepository {
        val api = RetrofitFactory.create()
        return ImageRepository(
            ImageRemoteDataStore(api),
            ImageCacheDataStore(WatchDogDatabase.getDatabase(context))
        )
    }
}
