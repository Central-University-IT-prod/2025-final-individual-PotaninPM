package com.potaninpm.feature_home.data.repository

import com.potaninpm.feature_home.data.remote.api.FinnhubApi
import com.potaninpm.feature_home.data.remote.api.SupabaseTickersApi
import com.potaninpm.feature_home.data.remote.dto.TickerDto
import com.potaninpm.feature_home.domain.model.Ticker
import com.potaninpm.feature_home.domain.repository.TickerRepository

class TickerRepositoryImpl(
    private val tickerApi: SupabaseTickersApi,
    private val finnhubApi: FinnhubApi
) : TickerRepository {

    override suspend fun getTickers(): List<TickerDto> {
        return try {
            tickerApi.getTickers()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun getTickerInfo(symbol: String): Ticker {
        return try {
            val quote = finnhubApi.getQuote(symbol)
            val profile = finnhubApi.getProfile(symbol)

            Ticker(
                symbol = symbol,
                companyName = profile.name,
                currentPrice = quote.currentPrice,
                currency = profile.currency,
                change = quote.change,
                changePercent = quote.changePercent,
                logoUrl = profile.logo
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Ticker(
                symbol = symbol,
                companyName = "",
                currentPrice = 0.0f,
                change = 0.0f,
                changePercent = 0.0f,
                logoUrl = "",
                currency = ""
            )
        }
    }


    override suspend fun getTickersInfo(symbols: List<String>): List<Ticker> {
        val tickers = symbols.map { getTickerInfo(it) }

        return if (tickers.all { it.currentPrice == 0.0f }) {
            emptyList()
        } else {
            tickers
        }
    }

    override suspend fun searchTickers(query: String): List<Ticker> {
        return try {
            val response = finnhubApi.searchTickers(query = query, exchange = "US")

            val symbols = response.result.map { it.symbol }.take(5)

            getTickersInfo(symbols)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}