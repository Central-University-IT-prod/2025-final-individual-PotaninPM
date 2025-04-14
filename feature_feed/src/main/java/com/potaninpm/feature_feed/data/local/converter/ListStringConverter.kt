package com.potaninpm.feature_feed.data.local.converter

import androidx.room.TypeConverter

class ListStringConverter {
    @TypeConverter
    fun fromList(list: List<String>?): String = list?.joinToString("||") ?: ""

    @TypeConverter
    fun toList(data: String): List<String> =
        if (data.isBlank()) emptyList() else data.split("||")
}