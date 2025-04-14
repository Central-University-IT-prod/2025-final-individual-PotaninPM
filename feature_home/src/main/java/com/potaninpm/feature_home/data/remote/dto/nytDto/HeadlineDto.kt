package com.potaninpm.feature_home.data.remote.dto.nytDto

import com.google.gson.annotations.SerializedName

data class HeadlineDto(
    @SerializedName("main") val main: String
)
