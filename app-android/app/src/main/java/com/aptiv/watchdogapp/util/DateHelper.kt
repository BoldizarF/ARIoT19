package com.aptiv.watchdogapp.util

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    fun formatTimestamp(timestamp: Long, includeOnlyDay: Boolean = false): String {
        // the format of your date
        val sdf = if (includeOnlyDay) {
            SimpleDateFormat("EEE HH:mm")
        } else {
            SimpleDateFormat("yyyy-MM-dd HH:mm")
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
