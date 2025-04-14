package com.potaninpm.feature_feed.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.potaninpm.feature_feed.data.local.entities.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Insert
    suspend fun insertPost(post: PostEntity): Long

    @Update
    suspend fun updatePost(post: PostEntity)

    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAllPostsFlow(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE isFavorite = 1 ORDER BY id DESC")
    fun getFavoritePostsFlow(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE author = :currentUser ORDER BY id DESC")
    fun getUserPostsFlow(currentUser: String): Flow<List<PostEntity>>
}