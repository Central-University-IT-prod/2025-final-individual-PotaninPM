package com.potaninpm.feature_home.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_articles")
data class NewsArticleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val abstract: String,
    val webUrl: String,
    val imageUrl: String?,
    val source: String,
    val publishedAt: String,
    val fetchedAt: Long
)
