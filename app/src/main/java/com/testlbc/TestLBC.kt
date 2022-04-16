package com.testlbc

import android.app.Application
import com.testlbc.data.repository.SongRepository
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class TestLBC : Application() {

    private val songRepository: SongRepository by inject()

    private var disposable: Disposable? = null

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@TestLBC)
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            modules(listOf(viewModelModule, dataModule, networkModule))
        }

        disposable?.dispose()
        disposable = songRepository.synchronise()
            .subscribeOn(Schedulers.io())
            .subscribe({
                // ignore
            }, Timber::e)
    }
}
