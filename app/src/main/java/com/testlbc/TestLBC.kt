package com.testlbc

import android.app.Application
import com.testlbc.data.repository.SongRepository
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class TestLBC : Application() {

    private val songRepository: SongRepository by inject()

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@TestLBC)
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            modules(listOf(viewModelModule, dataModule, networkModule))
        }

        MainScope().launch { songRepository.synchronise() }
    }
}
