package com.potaninpm.feature_home.domain.model

data class SearchResults(
    val news: List<NewsArticle>,
    val tickers: List<Ticker>
)
