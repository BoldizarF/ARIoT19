package com.aptiv.watchdogapp

import android.app.AlertDialog
import android.content.Context
import com.aptiv.watchdogapp.data.health.HealthValue

class MedicalAssistManager(private val context: Context) {

    companion object {
        private const val BAD_MESSAGE = "Are you sure your family member is feeling fine?"
    }

    fun checkValues(values: List<HealthValue>) {
        val hasBadHeartRate = values
            .sortedBy {
                it.timestamp
            }
            .take(5)
            .filter { it.heartRate < 60 || it.heartRate > 170 }
            .any()

        val hasBadTemperature = values
            .sortedBy {
                it.timestamp
            }
            .take(5)
            .filter { it.temperature <= 35 || it.temperature >= 38  }
            .any()



        var message = ""
        if (hasBadHeartRate) {
            message += " Indication says that the heart rate is bad."
        }
        if (hasBadTemperature) {
            message += " Indication says that the body temperature is bad."
        }

        if (message.isNotEmpty()) {
            AlertDialog.Builder(context)
                .setTitle("Health warning!")
                .setMessage(BAD_MESSAGE + message)
                .setPositiveButton(android.R.string.yes) { dialog, which ->
                    // Continue with delete operation
                }
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        }
    }

}
