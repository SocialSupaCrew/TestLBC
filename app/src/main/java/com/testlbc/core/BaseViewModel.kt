package com.testlbc.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<S, P : EventPath.Path> : ViewModel() {

    abstract fun getState(): LiveData<S>
    abstract fun getNavigation(): LiveData<EventPath<P>>
    abstract fun navigateTo(path: EventPath<P>)
}
