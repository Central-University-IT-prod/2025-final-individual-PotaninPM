package com.potaninpm.feature_finances.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.potaninpm.feature_finances.data.local.dao.GoalDao
import com.potaninpm.feature_finances.data.local.dao.OperationDao
import com.potaninpm.feature_finances.data.local.entities.GoalEntity
import com.potaninpm.feature_finances.data.local.entities.OperationEntity

@Database(entities = [GoalEntity::class, OperationEntity::class], version = 3, exportSchema = false)
abstract class FinanceDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao
    abstract fun operationDao(): OperationDao

    companion object {
        @Volatile private var instance: FinanceDatabase? = null

        fun getInstance(context: Context): FinanceDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    FinanceDatabase::class.java, "finance_db"
                ).build().also { instance = it }
            }
        }
    }
}