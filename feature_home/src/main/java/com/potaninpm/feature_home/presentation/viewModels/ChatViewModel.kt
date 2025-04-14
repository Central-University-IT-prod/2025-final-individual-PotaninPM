package com.potaninpm.feature_home.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potaninpm.feature_home.domain.repository.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository
): ViewModel() {
    private val _chatAns = MutableStateFlow<String?>(null)
    val chatAns: StateFlow<String?> = _chatAns

    private val _isThinking = MutableStateFlow(false)
    val isThinking: StateFlow<Boolean> = _isThinking

    fun getChatAnswer(question: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isThinking.value = true

                val res = chatRepository.getChatAnswer(question)
                _chatAns.value = res.answer

                _isThinking.value = false
            } catch (e: Exception) {
                e.printStackTrace()
                _chatAns.value = "error: ${e.message}"
            }
        }
    }

    fun reset() {
        _chatAns.value = null
        _isThinking.value = false
    }
}