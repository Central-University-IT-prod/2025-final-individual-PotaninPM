package com.potaninpm.feature_finances.domain

sealed class OperationType(val type: String) {
    data object DEPOSIT : OperationType("deposit")
    data object WITHDRAWAL : OperationType("withdrawal")
    data object TRANSFER : OperationType("transfer")
    data object DELETE : OperationType("delete")
}