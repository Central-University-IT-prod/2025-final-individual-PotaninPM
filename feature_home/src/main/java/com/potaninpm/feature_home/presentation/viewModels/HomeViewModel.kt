package com.potaninpm.feature_home.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.potaninpm.feature_home.domain.model.NewsArticle
import com.potaninpm.feature_home.domain.model.Ticker
import com.potaninpm.feature_home.domain.repository.NewsRepository
import com.potaninpm.feature_home.domain.repository.TickerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val tickerRepository: TickerRepository,
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _tickers = MutableStateFlow<List<Ticker>>(emptyList())
    val tickers: StateFlow<List<Ticker>> = _tickers

    private val _news = MutableStateFlow<List<NewsArticle>>(emptyList())
    val news: StateFlow<List<NewsArticle>> = _news

    private val _newTickerDataLoaded = MutableStateFlow(false)
    val newTickerDataLoaded: StateFlow<Boolean> = _newTickerDataLoaded

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val tickersDeferred = async { tickerRepository.getTickersInfo(tickerRepository.getTickers().map { it.ticker }) }
                val newsDeferred = async { newsRepository.getLatestNews() }

                val tickers = tickersDeferred.await()
                val news = newsDeferred.await()

                _tickers.value = tickers
                _news.value = news
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadNewsByCategory(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _news.value = emptyList()

            val articles = newsRepository.getArticlesByCategory(category)
            _news.value = articles
        }
    }

    fun refreshTickersData() {
        viewModelScope.launch(Dispatchers.IO) {
            val tickerSymbols = tickerRepository.getTickers().map { it.ticker }

            val tickersDeferred = async { tickerRepository.getTickersInfo(tickerSymbols) }

            _tickers.value = tickersDeferred.await()
            _newTickerDataLoaded.value = true

        }
    }

    fun refreshNewsData() {
        viewModelScope.launch(Dispatchers.IO) {
            val newsDeferred = async { newsRepository.getLatestNews() }
            _news.value = newsDeferred.await()
        }
    }
}

