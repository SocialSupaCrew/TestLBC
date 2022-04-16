package com.testlbc.core

import androidx.lifecycle.Observer
import com.testlbc.core.EventPath.Path

open class EventPath<out Path>(private val path: Path) {

    private var isConsume = false

    /**
     * Returns the path and prevents its use again.
     */
    fun getPathIfNotConsume(): Path? {
        return if (isConsume) {
            null
        } else {
            isConsume = true
            path
        }
    }

    interface Path
}

class EventPathObserver(private val consumePath: (Path) -> Unit) : Observer<EventPath<Path>> {
    override fun onChanged(event: EventPath<Path>?) {
        event?.getPathIfNotConsume()?.let(consumePath)
    }
}
