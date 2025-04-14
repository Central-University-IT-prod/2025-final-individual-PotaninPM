package com.potaninpm.feature_finances.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.potaninpm.feature_finances.data.local.entities.OperationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OperationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOperation(operation: OperationEntity): Long

    @Query("SELECT * FROM operations ORDER BY date DESC")
    fun getOperations(): Flow<List<OperationEntity>>
}