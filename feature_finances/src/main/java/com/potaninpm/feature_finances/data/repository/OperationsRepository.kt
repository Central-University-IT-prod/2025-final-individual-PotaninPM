package com.potaninpm.feature_finances.data.repository

import com.potaninpm.feature_finances.data.local.database.FinanceDatabase
import com.potaninpm.feature_finances.data.local.entities.OperationEntity
import kotlinx.coroutines.flow.Flow

class OperationsRepository(
    private val db: FinanceDatabase
) {
    fun getOperations(): Flow<List<OperationEntity>> = db.operationDao().getOperations()

    suspend fun addOperation(operation: OperationEntity): Long = db.operationDao().insertOperation(operation)
}