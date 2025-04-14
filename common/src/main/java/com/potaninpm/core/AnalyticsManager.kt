package com.potaninpm.core

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object AnalyticsManager {
    private const val TAG = "AnalyticsManager"
    private val locale: Locale by lazy { Locale.getDefault() }
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale)

    data class AnalyticsEvent(
        val eventName: String,
        val properties: Map<String, Any?> = emptyMap(),
        val timestamp: Long = System.currentTimeMillis()
    ) {
        override fun toString(): String {
            val formattedTime = dateFormat.format(Date(timestamp))
            return "[$formattedTime] $eventName: $properties"
        }
    }

    fun logEvent(eventName: String, properties: Map<String, Any?> = emptyMap()) {
        val event = AnalyticsEvent(eventName, properties)

        Log.d(TAG, event.toString())
    }
}
