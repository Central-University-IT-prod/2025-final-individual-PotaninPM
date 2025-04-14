package com.potaninpm.feature_home.data.remote.api

import com.potaninpm.core.ApiConstants
import com.potaninpm.feature_home.data.remote.dto.nytDto.NYTimesArticleResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NYTimesApi {

    @GET("articlesearch.json")
    suspend fun getArticles(
        @Query("q") query: String = "",
        @Query("page") page: Int = 0,
        @Query("api-key") apiKey: String = ApiConstants.NYTIMES_API_KEY
    ): NYTimesArticleResponseDto
}