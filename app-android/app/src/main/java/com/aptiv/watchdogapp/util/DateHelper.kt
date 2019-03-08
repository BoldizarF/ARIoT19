package com.aptiv.watchdogapp.util

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    fun formatTimestamp(timestamp: Long, simpleFormat: Boolean = true): String {
        // the format of your date
        val sdf = if (simpleFormat) {
            SimpleDateFormat("EEE HH:mm:ss")
        } else {
            SimpleDateFormat("EEE MM/dd HH:mm:ss")
        }
        // give a timezone reference for formatting
        sdf.timeZone = java.util.TimeZone.getTimeZone("GMT+1")
        return sdf.format(parseTimestamp(timestamp))
    }

    fun parseTimestamp(timestamp: Long): Date {
        // convert seconds to milliseconds
        return Date(timestamp * 1000L)
    }
}
