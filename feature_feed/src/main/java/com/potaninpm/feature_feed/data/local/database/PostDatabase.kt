package com.potaninpm.feature_feed.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.potaninpm.feature_feed.data.local.converter.ByteArrayListConverter
import com.potaninpm.feature_feed.data.local.converter.ListStringConverter
import com.potaninpm.feature_feed.data.local.dao.CommentDao
import com.potaninpm.feature_feed.data.local.dao.PostDao
import com.potaninpm.feature_feed.data.local.entities.CommentEntity
import com.potaninpm.feature_feed.data.local.entities.PostEntity

@Database(entities = [PostEntity::class, CommentEntity::class], version = 2, exportSchema = false)
@TypeConverters(ListStringConverter::class, ByteArrayListConverter::class)
abstract class PostDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao
}