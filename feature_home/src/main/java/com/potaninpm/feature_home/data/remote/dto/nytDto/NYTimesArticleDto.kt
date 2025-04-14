package com.potaninpm.feature_home.data.remote.dto.nytDto

import com.google.gson.annotations.SerializedName

data class NYTimesArticleDto(
    @SerializedName("_id") val id: String,
    @SerializedName("headline") val headline: HeadlineDto,
    @SerializedName("abstract") val abstract: String?,
    @SerializedName("web_url") val webUrl: String,
    @SerializedName("multimedia") val multimedia: List<MultimediaDto>?,
    @SerializedName("source") val source: String,
    @SerializedName("pub_date") val pubDate: String
)
