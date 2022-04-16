package com.testlbc

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TestLBC : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TestLBC)
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            modules(listOf(viewModelModule, dataModule, networkModule))
        }
    }
}
