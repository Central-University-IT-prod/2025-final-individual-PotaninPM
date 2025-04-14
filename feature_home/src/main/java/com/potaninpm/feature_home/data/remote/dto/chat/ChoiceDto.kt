package com.potaninpm.feature_home.data.remote.dto.chat

import com.google.gson.annotations.SerializedName

data class ChoiceDto(
    @SerializedName("logprobs") val logprobs: Any?,
    @SerializedName("finish_reason") val finishReason: String?,
    @SerializedName("native_finish_reason") val nativeFinishReason: String?,
    @SerializedName("index") val index: Int,
    @SerializedName("message") val message: ChatMessageDto
)
