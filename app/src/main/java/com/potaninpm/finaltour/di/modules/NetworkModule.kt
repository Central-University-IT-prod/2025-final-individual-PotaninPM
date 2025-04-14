package com.potaninpm.finaltour.di.modules

import com.potaninpm.core.ApiConstants
import com.potaninpm.feature_home.data.remote.api.FinnhubApi
import com.potaninpm.feature_home.data.remote.api.NYTimesApi
import com.potaninpm.feature_home.data.remote.api.SupabaseTickersApi
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val homeNetworkModule = module {
    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single(named("finnhubRetrofit")) {
        Retrofit.Builder()
            .baseUrl("https://finnhub.io/api/v1/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>(named("finnhubRetrofit")).create(FinnhubApi::class.java) }

    single(named("nytimesRetrofit")) {
        Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/svc/search/v2/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<NYTimesApi> { get<Retrofit>(named("nytimesRetrofit")).create(NYTimesApi::class.java) }

}

val postsNetworkModule = module {

    single {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("apikey", ApiConstants.SUPABASE_API_KEY)
                    .addHeader("Authorization", "Bearer ${ApiConstants.SUPABASE_API_KEY}")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<SupabaseTickersApi> { get<Retrofit>().create(SupabaseTickersApi::class.java) }
}