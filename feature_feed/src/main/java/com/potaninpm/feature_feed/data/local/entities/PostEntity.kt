package com.potaninpm.feature_feed.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val imagePaths: List<String>,
    val tags: List<String>,
    val webUrl: String = "",
    val webTitle: String = "",
    val webImageUrl: String = "",
    val isFavorite: Boolean = false,
    val author: String,
    val date: Long = System.currentTimeMillis(),
    val likes: Int = 0
)
