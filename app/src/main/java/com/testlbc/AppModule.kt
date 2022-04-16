package com.testlbc

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.testlbc.core.network.RetrofitInterceptor
import com.testlbc.core.network.repository.remote.SongService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://static.leboncoin.fr"

val viewModelModule: Module = module {

}

val dataModule: Module = module {

}

val networkModule: Module = module {
    single { retrofit(get(), get()) }
    single { httpClient(get(), get()) }
    single { retrofitInterceptor() }
    single { httpLoggingInterceptor(get()) }
    single { httpLoggingLevel() }
    single { gson() }

    single { songService(get()) }
}

private fun retrofit(client: OkHttpClient, gson: Gson) = Retrofit.Builder()
    .client(client)
    .baseUrl(BASE_URL)
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

private fun httpClient(
    httpLoggingInterceptor: HttpLoggingInterceptor,
    retrofitInterceptor: RetrofitInterceptor
) =
    OkHttpClient.Builder()
        .addInterceptor(retrofitInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .build()

private fun retrofitInterceptor() = RetrofitInterceptor()

private fun httpLoggingInterceptor(level: HttpLoggingInterceptor.Level) =
    HttpLoggingInterceptor().also { it.level = level }

private fun httpLoggingLevel(): HttpLoggingInterceptor.Level =
    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

private fun gson(): Gson = GsonBuilder().create()

private fun songService(retrofit: Retrofit) = retrofit.create(SongService::class.java)