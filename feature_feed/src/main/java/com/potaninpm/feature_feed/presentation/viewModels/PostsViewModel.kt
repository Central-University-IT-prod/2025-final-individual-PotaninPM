package com.potaninpm.feature_feed.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potaninpm.feature_feed.data.local.entities.PostEntity
import com.potaninpm.feature_feed.data.repository.PostRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PostsViewModel(
    private val repository: PostRepository
): ViewModel() {
    private val currentUser: String = "user1"

    val allPostsFlow: StateFlow<List<PostEntity>> = repository.getAllPostsFlow().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val favoritePostsFlow: StateFlow<List<PostEntity>> = repository.getFavoritePostsFlow().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val myPostsFlow: StateFlow<List<PostEntity>> = repository.getMyPostsFlow(currentUser).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addPost(
        webUrl: String = "",
        webTitle: String = "",
        webImageUrl: String = "",
        text: String,
        imagePaths: List<String>,
        tags: List<String>
    ) {
        viewModelScope.launch {
            repository.addPost(
                PostEntity(
                    webUrl = webUrl,
                    text = text,
                    webTitle = webTitle,
                    webImageUrl = webImageUrl,
                    imagePaths = imagePaths,
                    tags = tags,
                    author = currentUser
                )
            )
        }
    }

    fun favoritePost(post: PostEntity) {
        viewModelScope.launch {
            val updatedLikes = if (!post.isFavorite) post.likes + 1 else (post.likes - 1).coerceAtLeast(0)
            val updatedPost = post.copy(isFavorite = !post.isFavorite, likes = updatedLikes)

            repository.updatePost(updatedPost)
        }
    }
}