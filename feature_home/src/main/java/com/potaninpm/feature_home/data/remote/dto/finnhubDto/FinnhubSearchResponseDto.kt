package com.potaninpm.feature_home.data.remote.dto.finnhubDto

import com.google.gson.annotations.SerializedName

data class FinnhubSearchResponseDto(
    @SerializedName("count") val count: Int,
    @SerializedName("result") val result: List<FinnhubSearchResultDto>
)
