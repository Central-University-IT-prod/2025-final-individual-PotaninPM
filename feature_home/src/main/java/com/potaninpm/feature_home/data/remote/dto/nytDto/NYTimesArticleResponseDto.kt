package com.potaninpm.feature_home.data.remote.dto.nytDto

import com.google.gson.annotations.SerializedName

data class NYTimesArticleResponseDto(
    @SerializedName("response") val response: NYTimesResponse
)
