package com.potaninpm.feature_home.data.remote.api

import com.potaninpm.feature_home.data.remote.dto.TickerDto
import retrofit2.http.GET

interface SupabaseTickersApi {

    @GET("rest/v1/tickers?select=ticker")
    suspend fun getTickers(): List<TickerDto>
}