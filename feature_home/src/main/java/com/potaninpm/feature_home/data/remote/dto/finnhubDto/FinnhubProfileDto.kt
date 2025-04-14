package com.potaninpm.feature_home.data.remote.dto.finnhubDto

import com.google.gson.annotations.SerializedName

data class FinnhubProfileDto(
    @SerializedName("name") val name: String,
    @SerializedName("logo") val logo: String?,
    @SerializedName("currency") val currency: String
)
