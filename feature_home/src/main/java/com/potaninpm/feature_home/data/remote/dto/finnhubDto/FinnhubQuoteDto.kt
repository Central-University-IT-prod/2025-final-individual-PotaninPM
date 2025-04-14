package com.potaninpm.feature_home.data.remote.dto.finnhubDto

import com.google.gson.annotations.SerializedName

data class FinnhubQuoteDto(
    @SerializedName("c") val currentPrice: Float,
    @SerializedName("d") val change: Float,
    @SerializedName("dp") val changePercent: Float
)
