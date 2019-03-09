package com.aptiv.watchdogapp.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {

    companion object {
        @Volatile
        private var INSTANCE: WatchDogApiService? = null

        fun getInstance(): WatchDogApiService {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Retrofit.Builder()
                    .baseUrl(WatchDogApiService.ENDPOINT)
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                    .build()
                    .create(WatchDogApiService::class.java)

                INSTANCE = instance
                return instance
            }
        }
    }
}
