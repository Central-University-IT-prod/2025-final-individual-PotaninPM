package com.potaninpm.feature_feed.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val postId: Long,
    val author: String,
    val text: String,
    val date: Long = System.currentTimeMillis(),
    val likes: Int = 0,
    val dislikes: Int = 0
)
