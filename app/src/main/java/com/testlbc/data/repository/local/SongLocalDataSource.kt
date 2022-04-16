package com.testlbc.data.repository.local

import com.testlbc.data.repository.remote.SongJson
import io.reactivex.Flowable

interface SongLocalDataSource {

    fun save(songs: List<SongJson>)
    fun get(): Flowable<List<Song>>
}
