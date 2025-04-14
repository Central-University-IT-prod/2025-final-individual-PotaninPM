package com.potaninpm.feature_home.domain.repository

import com.potaninpm.feature_home.data.remote.dto.TickerDto
import com.potaninpm.feature_home.domain.model.Ticker

interface TickerRepository {
    suspend fun getTickers(): List<TickerDto>

    suspend fun getTickerInfo(symbol: String): Ticker
    suspend fun getTickersInfo(symbols: List<String>): List<Ticker>

    suspend fun searchTickers(query: String): List<Ticker>
}