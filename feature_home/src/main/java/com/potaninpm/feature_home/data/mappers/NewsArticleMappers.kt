package com.potaninpm.feature_home.data.mappers

import com.potaninpm.feature_home.data.local.entity.NewsArticleEntity
import com.potaninpm.feature_home.domain.model.NewsArticle

fun NewsArticle.toEntity(fetchedAt: Long): NewsArticleEntity {
    return NewsArticleEntity(
        id = id,
        title = title,
        abstract = abstract,
        webUrl = webUrl,
        imageUrl = imageUrl,
        source = source,
        publishedAt = publishedAt,
        fetchedAt = fetchedAt
    )
}

fun NewsArticleEntity.toDomainNews(): NewsArticle {
    return NewsArticle(
        id = id,
        title = title,
        abstract = abstract,
        webUrl = webUrl,
        imageUrl = imageUrl,
        source = source,
        publishedAt = publishedAt
    )
}
