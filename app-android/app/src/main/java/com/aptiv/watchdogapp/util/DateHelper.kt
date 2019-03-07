package com.aptiv.watchdogapp.util

import java.util.*

object DateHelper {

    fun formatTimestamp(timestamp: Long): String {
        // the format of your date
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
        // give a timezone reference for formatting (see comment at the bottom)
        sdf.timeZone = java.util.TimeZone.getTimeZone("GMT+1")
        return sdf.format(parseTimestamp(timestamp))
    }

    fun parseTimestamp(timestamp: Long): Date {
        // convert seconds to milliseconds
        return Date(timestamp * 1000L)
    }
}
