package com.potaninpm.feature_finances.data.repository

import com.potaninpm.feature_finances.data.local.database.FinanceDatabase
import com.potaninpm.feature_finances.data.local.entities.GoalEntity
import kotlinx.coroutines.flow.Flow

class GoalsRepository(
    private val db: FinanceDatabase
) {
    fun getGoals(): Flow<List<GoalEntity>> = db.goalDao().getGoals()

    suspend fun addGoal(goal: GoalEntity): Long = db.goalDao().insertGoal(goal)
    suspend fun updateGoal(goal: GoalEntity) = db.goalDao().updateGoal(goal)
    suspend fun deleteGoal(goal: GoalEntity) = db.goalDao().deleteGoal(goal)
}