package com.potaninpm.feature_home.data.remote.dto.finnhubDto

import com.google.gson.annotations.SerializedName

data class FinnhubSearchResultDto(
    @SerializedName("symbol") val symbol: String,
    @SerializedName("description") val description: String,
    @SerializedName("displaySymbol") val displaySymbol: String,
    @SerializedName("type") val type: String
)
