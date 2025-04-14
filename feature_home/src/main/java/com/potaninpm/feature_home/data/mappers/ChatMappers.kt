package com.potaninpm.feature_home.data.mappers

import com.potaninpm.feature_home.data.remote.dto.chat.ChatResponse
import com.potaninpm.feature_home.domain.model.ChatAnswer

fun ChatResponse.toDomain(): ChatAnswer {
    val ans = choices.firstOrNull()?.message?.content ?: "no_ans"
    return ChatAnswer(ans)
}