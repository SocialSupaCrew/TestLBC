package com.testlbc

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.testlbc.core.network.RetrofitInterceptor
import com.testlbc.data.interactor.*
import com.testlbc.data.repository.SongRepository
import com.testlbc.data.repository.SongRepositoryImpl
import com.testlbc.data.repository.local.AppDatabase
import com.testlbc.data.repository.local.SongLocalDataSource
import com.testlbc.data.repository.local.SongLocalDataSourceImpl
import com.testlbc.data.repository.remote.SongRemoteDataSource
import com.testlbc.data.repository.remote.SongRemoteDataSourceImpl
import com.testlbc.data.repository.remote.SongService
import com.testlbc.ui.albumdetail.AlbumDetailViewModel
import com.testlbc.ui.albumdetail.AlbumDetailViewModelImpl
import com.testlbc.ui.albumlist.AlbumListViewModel
import com.testlbc.ui.albumlist.AlbumListViewModelImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://static.leboncoin.fr"

val viewModelModule: Module = module {
    viewModel<AlbumListViewModel> {
        AlbumListViewModelImpl(MediatorLiveData(), MutableLiveData(), get())
    }
    viewModel<AlbumDetailViewModel> { (albumId: Int) ->
        AlbumDetailViewModelImpl(MediatorLiveData(), MutableLiveData(), get(), albumId)
    }
}

val dataModule: Module = module {
    factory<GetAlbumsInteractor> { GetAlbumsInteractorImpl(get(), get()) }
    factory<GetAlbumInteractor> { GetAlbumInteractorImpl(get()) }
    factory { AlbumMapper() }

    single<SongRepository> { SongRepositoryImpl(get(), get()) }
    single<SongRemoteDataSource> { SongRemoteDataSourceImpl(get()) }
    single<SongLocalDataSource> { SongLocalDataSourceImpl(get()) }

    single { roomDatabase(get()) }
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

private fun roomDatabase(application: Application) =
    Room.databaseBuilder(application, AppDatabase::class.java, AppDatabase.DATABASE_NAME).build()

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
