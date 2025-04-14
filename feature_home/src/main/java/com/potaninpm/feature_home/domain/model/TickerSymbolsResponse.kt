package com.potaninpm.feature_home.domain.model

import com.google.gson.annotations.SerializedName

data class TickerSymbolsResponse(
    @SerializedName("tickerSymbols")
    val tickerSymbols: List<String>
)
