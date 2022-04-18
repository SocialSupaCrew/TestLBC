package com.testlbc.core.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract class BaseInteractor<T>(
    protected val liveData: MutableLiveData<T> = MutableLiveData()
) : Interactor<T> {

    override fun getLiveData(): LiveData<T> = liveData
}
