package com.potaninpm.feature_home.domain.model

data class Ticker(
    val symbol: String,
    val companyName: String?,
    val currentPrice: Float,
    val currency: String,
    val change: Float,
    val changePercent: Float,
    val logoUrl: String?
)
