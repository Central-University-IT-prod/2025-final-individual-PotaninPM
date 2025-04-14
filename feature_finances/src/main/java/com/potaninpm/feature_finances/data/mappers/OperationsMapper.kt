package com.potaninpm.feature_finances.data.mappers

import com.potaninpm.feature_finances.data.local.entities.OperationEntity
import com.potaninpm.feature_finances.domain.Operation

fun OperationEntity.toDomain(): Operation {
    return Operation(
        date = this.date,
        title = this.title,
        amount = this.amount,
        type = this.type,
        currency = this.currency,
        comment = this.comment,
    )
}