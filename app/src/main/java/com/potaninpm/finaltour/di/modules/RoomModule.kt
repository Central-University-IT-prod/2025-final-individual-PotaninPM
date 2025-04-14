package com.potaninpm.finaltour.di.modules

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import com.potaninpm.feature_feed.data.local.database.PostDatabase
import com.potaninpm.feature_finances.data.local.database.FinanceDatabase
import com.potaninpm.feature_home.data.local.database.NewsDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val financeRoomModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            FinanceDatabase::class.java,
            "finance_database.db"
        ).fallbackToDestructiveMigration().build()
    }
    single { FinanceDatabase.getInstance(get()) }

    single {
        get<FinanceDatabase>().goalDao()
    }

    single {
        get<FinanceDatabase>().operationDao()
    }
}

val postsRoomModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            PostDatabase::class.java,
            "posts_database.db"
        ).fallbackToDestructiveMigration().build()
    }

    single {
        get<PostDatabase>().postDao()
    }

    single {
        get<PostDatabase>().commentDao()
    }
}

val newsRoomModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            NewsDatabase::class.java,
            "news_database.db"
        ).fallbackToDestructiveMigration().build()
    }

    single {
        get<NewsDatabase>().newsDao()
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences("news_prefs", Context.MODE_PRIVATE)
    }
}