package com.potaninpm.feature_home.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.potaninpm.feature_home.data.local.entity.NewsArticleEntity

@Dao
interface NewsArticleDao {
    @Query("SELECT * FROM news_articles")
    suspend fun getAllNews(): List<NewsArticleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<NewsArticleEntity>)

    @Query("DELETE FROM news_articles")
    suspend fun clearNews()
}