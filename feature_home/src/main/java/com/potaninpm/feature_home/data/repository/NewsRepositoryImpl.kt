package com.potaninpm.feature_home.data.repository

import android.content.SharedPreferences
import com.potaninpm.feature_home.data.local.dao.NewsArticleDao
import com.potaninpm.feature_home.data.mappers.toDomainNews
import com.potaninpm.feature_home.data.mappers.toEntity
import com.potaninpm.feature_home.data.remote.api.NYTimesApi
import com.potaninpm.feature_home.domain.model.NewsArticle
import com.potaninpm.feature_home.domain.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRepositoryImpl(
    private val nyTimesApi: NYTimesApi,
    private val newsArticleDao: NewsArticleDao,
    private val prefs: SharedPreferences
) : NewsRepository {
    private val LAST_UPDATE_KEY = "news_cache_last_update"

    // six hours in millisec
    private val SIX_HOURS = 6 * 60 * 60 * 1000L

    override suspend fun getLatestNews(): List<NewsArticle> = withContext(Dispatchers.IO) {
        val currentTime = System.currentTimeMillis()

        return@withContext try {
            val response = nyTimesApi.getArticles()
            val articles = response.response.docs.map { dto ->
                dto.toDomainNews()
            }

            newsArticleDao.clearNews()
            newsArticleDao.insertAll(articles.map { it.toEntity(currentTime) })
            prefs.edit().putLong(LAST_UPDATE_KEY, currentTime).apply()
            articles
        } catch (e: Exception) {
            e.printStackTrace()
            val lastUpdateTime = prefs.getLong(LAST_UPDATE_KEY, 0L)
            if (currentTime - lastUpdateTime < SIX_HOURS) {
                val cachedNews = newsArticleDao.getAllNews()
                cachedNews.map { it.toDomainNews() }
            } else {
                emptyList()
            }
        }
    }

    override suspend fun getArticlesByCategory(category: String): List<NewsArticle> = withContext(Dispatchers.IO) {
        try {
            val response = nyTimesApi.getArticles(query = category)
            val articles = response.response.docs.map { dto ->
                dto.toDomainNews()
            }
            articles
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun searchNews(query: String, page: Int): List<NewsArticle> {
        try {
            val response = nyTimesApi.getArticles(query = query, page = page)
            val articles = response.response.docs.map { dto ->
                dto.toDomainNews()
            }
            return articles
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }
}