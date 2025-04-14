package com.potaninpm.feature_feed.data.repository

import com.potaninpm.feature_feed.data.local.database.PostDatabase
import com.potaninpm.feature_feed.data.local.entities.CommentEntity
import com.potaninpm.feature_feed.data.local.entities.PostEntity
import kotlinx.coroutines.flow.Flow

class PostRepository(
    private val db: PostDatabase
) {
    fun getAllPostsFlow(): Flow<List<PostEntity>> = db.postDao().getAllPostsFlow()
    fun getFavoritePostsFlow(): Flow<List<PostEntity>> = db.postDao().getFavoritePostsFlow()
    fun getMyPostsFlow(currentUser: String): Flow<List<PostEntity>> = db.postDao().getUserPostsFlow(currentUser)

    suspend fun addPost(post: PostEntity) = db.postDao().insertPost(post)
    suspend fun updatePost(post: PostEntity) = db.postDao().updatePost(post)
    suspend fun addComment(comment: CommentEntity) = db.commentDao().insertComment(comment)

    fun getCommentsForPost(postId: Long): Flow<List<CommentEntity>> = db.commentDao().getComments(postId)
    suspend fun likeOrDislikeComment(comment: CommentEntity) = db.commentDao().updateComment(comment)
}