package com.potaninpm.feature_feed.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potaninpm.feature_feed.data.local.entities.CommentEntity
import com.potaninpm.feature_feed.data.repository.PostRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CommentsViewModel(
    private val repository: PostRepository,
    private val postId: Long
) : ViewModel() {
    private val currentUsername: String = "author1"

    val comments: StateFlow<List<CommentEntity>> =
        repository.getCommentsForPost(postId)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addComment(commentText: String) {
        viewModelScope.launch {
            repository.addComment(
                CommentEntity(
                    postId = postId,
                    author = currentUsername,
                    text = commentText
                )
            )
        }
    }
}
