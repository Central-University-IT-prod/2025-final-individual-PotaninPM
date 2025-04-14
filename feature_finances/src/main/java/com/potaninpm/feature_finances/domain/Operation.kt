package com.potaninpm.feature_finances.domain

data class Operation(
    val date: Long,
    val title: String,
    val type: String,
    val currency: String,
    val amount: Double,
    val comment: String? = null
)