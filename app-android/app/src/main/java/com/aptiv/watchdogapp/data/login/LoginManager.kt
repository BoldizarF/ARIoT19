package com.aptiv.watchdogapp.data.login

import android.util.Log
import com.aptiv.watchdogapp.data.WatchDogApiService
import retrofit2.Call
import retrofit2.Response

class LoginManager

    constructor(
        private val api: WatchDogApiService
    ) {

    fun login(key: String, onCallback: Callback) {
        api.login(key).enqueue(object : retrofit2.Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("LoginManager", "Remote call to Login failed", t)
            }
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    onCallback.afterLogin(response.body() ?: "")
                } else {
                    onCallback.afterLogin("")
                }
            }
        })
    }

    interface Callback {
        fun afterLogin(apiKey: String)
    }
}
