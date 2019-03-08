package com.aptiv.watchdogapp

import android.app.AlertDialog
import android.content.Context
import com.aptiv.watchdogapp.data.health.HealthValue

class MedicalAssistManager(context: Context) {

    private var alertDialog: AlertDialog? = null
    private var alertDialogBuilder = AlertDialog.Builder(context)
        .setTitle("Health warning!")
        .setIcon(android.R.drawable.ic_dialog_alert)

    companion object {
        private const val BAD_MESSAGE = "Are you sure your family member is feeling fine?"
    }

    fun checkValues(values: List<HealthValue>) {
        val latestValues = values
            .sortedBy {
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
            message += "\n- Indication says that the heart rate is bad."
        }
        if (hasBadTemperature) {
            message += "\n- Indication says that the body temperature is bad."
        }

        if (alertDialog?.isShowing != true && message.isNotEmpty()) {
            alertDialog = alertDialogBuilder
                .setMessage(BAD_MESSAGE + message)
                .setPositiveButton(android.R.string.yes) { dialog, which ->
                    alertDialog?.dismiss()
                }
                .setNegativeButton(android.R.string.no) { dialog, which ->
                    alertDialog?.dismiss()
                }
                .create()

            alertDialog!!.show()
        }
    }
}
