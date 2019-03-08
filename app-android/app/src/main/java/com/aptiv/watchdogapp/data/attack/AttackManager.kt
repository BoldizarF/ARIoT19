package com.aptiv.watchdogapp.data.attack

import android.util.Log
import com.aptiv.watchdogapp.data.WatchDogApiService

class AttackManager

    constructor(
        private val api: WatchDogApiService
    ) {

    fun attack(): Boolean {
        return try {
            api.setAttackMode()
            true
        }
        catch (e: Exception) {
            Log.e("AttackManager", "Unable to attack", e)
            false
        }
    }
}
