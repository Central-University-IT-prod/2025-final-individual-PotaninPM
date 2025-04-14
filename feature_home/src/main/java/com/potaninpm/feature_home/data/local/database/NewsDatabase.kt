package com.potaninpm.feature_home.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.potaninpm.feature_home.data.local.dao.NewsArticleDao
import com.potaninpm.feature_home.data.local.entity.NewsArticleEntity

@Database(entities = [NewsArticleEntity::class], version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsArticleDao
}