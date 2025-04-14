package com.potaninpm.feature_home.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TickerDto(
    @SerializedName("ticker") val ticker: String
)