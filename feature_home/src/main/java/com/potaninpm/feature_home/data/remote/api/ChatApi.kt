package com.potaninpm.feature_home.data.remote.api

import com.potaninpm.feature_home.data.remote.dto.chat.ChatResponse
import com.potaninpm.feature_home.domain.model.ChatRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApi {
    @POST("api/v1/chat/completions")
    suspend fun getChatAnswer(
        @Body request: ChatRequest
    ): ChatResponse
}