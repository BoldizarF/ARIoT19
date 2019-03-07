package com.aptiv.watchdogapp.health

import android.content.Context

object HealthRepositoryFactory {

    fun create(context: Context): HealthRepository {
        return HealthRepository(
            HealthRemoteDataStore(),
            HealthCacheDataStore()
        )
    }
}
