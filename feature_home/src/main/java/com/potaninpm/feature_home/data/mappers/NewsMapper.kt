package com.potaninpm.feature_home.data.mappers

import com.potaninpm.feature_home.data.remote.dto.nytDto.NYTimesArticleDto
import com.potaninpm.feature_home.domain.model.NewsArticle
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun NYTimesArticleDto.toDomainNews(): NewsArticle {
    val imageUrl = multimedia?.firstOrNull { it.subtype == "xlarge" }?.url

    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")
    val outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy '|' HH:mm").withZone(ZoneId.systemDefault())
    val dateTime = OffsetDateTime.parse(pubDate, inputFormatter)

    val formattedDate = dateTime.format(outputFormatter)

    return NewsArticle(
        id = id,
        title = headline.main,
        abstract = abstract ?: "",
        webUrl = webUrl,
        imageUrl = imageUrl?.let { "https://www.nytimes.com/$it" },
        source = source ?: "",
        publishedAt = formattedDate
    )
}