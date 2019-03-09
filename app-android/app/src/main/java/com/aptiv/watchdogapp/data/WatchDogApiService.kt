package com.aptiv.watchdogapp.data

import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WatchDogApiService {

    companion object {
        const val ENDPOINT = "http://ariot-env.npn96gumht.us-east-2.elasticbeanstalk.com:80/api/v1/"
    }

    @GET("healthvalues")
    fun getLatestHealthValues(@Query("apikey") apiKey: String): Deferred<Map<Long, String>>

    @GET("images")
    fun getLatestImages(@Query("apikey") apiKey: String): Deferred<Map<Long, String>>

    @POST("attack")
    fun setAttackMode(@Query("apikey") apiKey: String = "000", @Body setAttack: Boolean = true): Deferred<Boolean>

    @POST("login")
    fun login(@Body passKey: String): Call<String>
}
