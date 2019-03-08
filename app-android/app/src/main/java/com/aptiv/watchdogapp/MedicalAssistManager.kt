package com.aptiv.watchdogapp

import android.app.AlertDialog
import android.content.Context
import com.aptiv.watchdogapp.data.health.HealthValue

class MedicalAssistManager {

    private var alertDialog: AlertDialog? = null

    companion object {
        private const val BAD_MESSAGE = "Are you sure your family member is feeling fine?"
    }

    fun checkValues(context: Context, values: List<HealthValue>) {
        val latestValues = values
            .sortedByDescending {
                it.timestamp
            }
            .take(5)

        val hasBadHeartRate = latestValues
            .filter { it.heartRate < 60 || it.heartRate > 170 }
            .any()

        val hasBadTemperature = latestValues
            .filter { it.temperature <= 35 || it.temperature >= 38  }
            .any()

        var message = ""
        if (hasBadHeartRate) {
            message += "\n   - Indication says that the heart rate is bad."
        }
        if (hasBadTemperature) {
            message += "\n   - Indication says that the body temperature is bad."
        }

        if (alertDialog?.isShowing != true && message.isNotEmpty()) {
            alertDialog = createAlert(context, message)
            alertDialog?.show()
        }
    }

    private fun createAlert(context: Context, message: String): AlertDialog {
        return AlertDialog.Builder(context)
            .setTitle("Health warning!")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setMessage(BAD_MESSAGE + message)
            .setPositiveButton(android.R.string.ok) { dialog, which -> }
            .create()
    }
}
