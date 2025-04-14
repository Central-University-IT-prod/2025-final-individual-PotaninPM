package com.potaninpm.feature_home.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potaninpm.feature_home.domain.model.SearchResults
import com.potaninpm.feature_home.domain.repository.NewsRepository
import com.potaninpm.feature_home.domain.repository.TickerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val newsRepository: NewsRepository,
    private val tickerRepository: TickerRepository
) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _searchResults = MutableStateFlow(SearchResults(emptyList(), emptyList()))
    val searchResults: StateFlow<SearchResults> = _searchResults

    private val _currentPage = MutableStateFlow(0)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _query
                .debounce(700)
                .distinctUntilChanged()
                .collect { q ->
                    if (q.isBlank()) {
                        _searchResults.value = SearchResults(emptyList(), emptyList())
                        _currentPage.value = 0
                    } else {
                        try {
                            searchNews(q, resetPage = true)
                        } catch (e: Exception) {
                            e.printStackTrace()

                            _searchResults.value = SearchResults(emptyList(), emptyList())
                        }
                    }
                }
        }
    }

    fun loadMore() {
        val query = _query.value
        if (query.isNotBlank()) {
            searchNews(query, resetPage = false)
        }
    }

    private fun searchNews(query: String, resetPage: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val nextPage = if (resetPage) 0 else _currentPage.value + 1

            try {
                val news = newsRepository.searchNews(query, nextPage)
                val tickers = if (resetPage) {
                    tickerRepository.searchTickers(query).take(10)
                } else {
                    _searchResults.value.tickers
                }

                val newArticles = if (resetPage) {
                    news
                } else {
                    _searchResults.value.news + news
                }

                _searchResults.value = SearchResults(newArticles, tickers)
                _currentPage.value = nextPage
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setQuery(newQuery: String) {
        _query.value = newQuery
    }

    fun clearAll() {
        _query.value = ""
        _searchResults.value = SearchResults(emptyList(), emptyList())
    }
}