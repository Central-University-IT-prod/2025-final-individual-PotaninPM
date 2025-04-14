package com.potaninpm.feature_home.data.remote.dto.chat

import com.google.gson.annotations.SerializedName

data class ChatMessageDto(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String
)
