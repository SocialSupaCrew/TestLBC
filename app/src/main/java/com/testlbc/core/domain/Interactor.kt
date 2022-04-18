package com.testlbc.core.domain

import androidx.lifecycle.LiveData

interface Interactor<T> {

    fun getLiveData(): LiveData<T>
}
