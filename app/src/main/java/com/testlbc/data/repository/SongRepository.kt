package com.testlbc.data.repository

import com.testlbc.data.repository.local.Song
import com.testlbc.data.repository.remote.SongJson
import io.reactivex.Flowable
import io.reactivex.Single

interface SongRepository {

    fun synchronise(): Single<List<SongJson>>
    fun get(): Flowable<List<Song>>
}
