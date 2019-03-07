package com.aptiv.watchdogapp

import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface WatchDogApiService {

    companion object {
        const val ENDPOINT = "http://ariot-env.npn96gumht.us-east-2.elasticbeanstalk.com:80/api/v1/"
    }

    @GET("healthvalues?apikey=555")
    fun getLatestHeartRateValues(): Deferred<Map<Long, Int>>

    @GET("images?apikey=555")
    fun getLatestImages():  Deferred<Map<Long, String>>

}
