package com.potaninpm.finaltour.di.modules

import com.potaninpm.core.ApiConstants
import com.potaninpm.feature_home.data.remote.api.ChatApi
import com.potaninpm.feature_home.domain.repository.ChatRepository
import com.potaninpm.feature_home.data.repository.ChatRepositoryImpl
import com.potaninpm.feature_home.presentation.viewModels.ChatViewModel
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val chatModule = module {
    single(named("chat")) {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer ${ApiConstants.AI_API_KEY}")
                    .build()

                chain.proceed(request)
            }
            .build()
    }

    single(named("chat")) {
        Retrofit.Builder()
            .baseUrl(ApiConstants.AI_BASE_URL)
            .client(get(named("chat")))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    viewModel { ChatViewModel(get()) }

   single<ChatRepository> { ChatRepositoryImpl(get()) }

    single<ChatApi> { get<Retrofit>(named("chat")).create(ChatApi::class.java) }
}