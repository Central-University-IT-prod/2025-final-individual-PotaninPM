package com.potaninpm.feature_home.domain.model

import com.potaninpm.feature_home.data.remote.dto.chat.ChatMessageDto

data class ChatRequest(
    val model: String,
    val messages: List<ChatMessageDto>
)
