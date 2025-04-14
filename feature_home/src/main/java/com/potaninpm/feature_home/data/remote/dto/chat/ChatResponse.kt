package com.potaninpm.feature_home.data.remote.dto.chat

import com.google.gson.annotations.SerializedName

data class ChatResponse(
    @SerializedName("id") val id: String,
    @SerializedName("provider") val provider: String,
    @SerializedName("model") val model: String,
    @SerializedName("object") val `object`: String,
    @SerializedName("created") val created: Long,
    @SerializedName("choices") val choices: List<ChoiceDto>
)
