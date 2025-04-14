package com.potaninpm.feature_home.domain.model

data class NewsArticle(
    val id: String,
    val title: String,
    val abstract: String,
    val webUrl: String,
    val imageUrl: String?,
    val source: String,
    val publishedAt: String
)
