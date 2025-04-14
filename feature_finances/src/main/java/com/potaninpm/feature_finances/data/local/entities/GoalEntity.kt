package com.potaninpm.feature_finances.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val targetAmount: Long,
    val currentAmount: Long = 0,
    val currency: String,
    val dueDate: Long? = null
)
