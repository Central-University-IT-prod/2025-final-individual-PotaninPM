package com.potaninpm.feature_home.domain.repository

import com.potaninpm.feature_home.domain.model.NewsArticle

interface NewsRepository {
    suspend fun getLatestNews(): List<NewsArticle>

    suspend fun getArticlesByCategory(category: String): List<NewsArticle>

    suspend fun searchNews(query: String, page: Int): List<NewsArticle>
}