package com.potaninpm.feature_home.data.remote.api

import com.potaninpm.core.ApiConstants
import com.potaninpm.feature_home.data.remote.dto.finnhubDto.FinnhubProfileDto
import com.potaninpm.feature_home.data.remote.dto.finnhubDto.FinnhubQuoteDto
import com.potaninpm.feature_home.data.remote.dto.finnhubDto.FinnhubSearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface FinnhubApi {

    @GET("quote")
    suspend fun getQuote(
        @Query("symbol") symbol: String,
        @Query("token") token: String = ApiConstants.FINNHUB_API_KEY
    ): FinnhubQuoteDto

    @GET("stock/profile2")
    suspend fun getProfile(
        @Query("symbol") symbol: String,
        @Query("token") token: String = ApiConstants.FINNHUB_API_KEY
    ): FinnhubProfileDto

    @GET("search")
    suspend fun searchTickers(
        @Query("q") query: String,
        @Query("exchange") exchange: String = "US",
        @Query("token") token: String = ApiConstants.FINNHUB_API_KEY
    ): FinnhubSearchResponseDto
}