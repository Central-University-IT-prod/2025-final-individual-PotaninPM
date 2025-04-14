package com.potaninpm.feature_home.data.remote.dto.nytDto

import com.google.gson.annotations.SerializedName

data class MultimediaDto(
    @SerializedName("url") val url: String,
    @SerializedName("subtype") val subtype: String
)