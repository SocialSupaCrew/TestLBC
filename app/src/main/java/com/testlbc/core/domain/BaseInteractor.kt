package com.testlbc.core.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseInteractor<T>(
    private val compositeDisposable: CompositeDisposable = CompositeDisposable(),
    protected val liveData: MutableLiveData<T> = MutableLiveData()
) : Interactor<T> {

    protected fun Disposable.track() {
        compositeDisposable.add(this)
    }

    override fun getLiveData(): LiveData<T> = liveData

    override fun cleanUp() {
        compositeDisposable.clear()
    }
}
