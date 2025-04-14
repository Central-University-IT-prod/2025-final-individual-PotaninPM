package com.potaninpm.feature_feed.data.local.converter

import android.util.Base64
import androidx.room.TypeConverter

class ByteArrayListConverter {
    @TypeConverter
    fun fromByteArrayList(list: List<ByteArray>?): String =
        list?.joinToString(separator = "||") { Base64.encodeToString(it, Base64.DEFAULT).trim() } ?: ""

    @TypeConverter
    fun toByteArrayList(data: String): List<ByteArray> =
        if (data.isBlank()) emptyList() else data.split("||").map { Base64.decode(it, Base64.DEFAULT) }
}