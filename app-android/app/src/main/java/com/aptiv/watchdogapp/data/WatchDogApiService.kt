package com.aptiv.watchdogapp.data

import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface WatchDogApiService {

    companion object {
        const val ENDPOINT = "http://ariot-env.npn96gumht.us-east-2.elasticbeanstalk.com:80/api/v1/"
    }

    @GET("healthvalues?apikey=555")
    fun getLatestHealthValues(): Deferred<Map<Long, String>>

    @GET("images?apikey=555")
    fun getLatestImages(): Deferred<Map<Long, String>>

    @POST("attack?apikey=000")
    fun setAttackMode(@Body setAttack: Boolean = true): Deferred<Boolean>
}
