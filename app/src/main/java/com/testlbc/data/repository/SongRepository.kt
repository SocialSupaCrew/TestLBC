package com.testlbc.data.repository

import com.testlbc.data.repository.local.Song
import com.testlbc.data.repository.local.SongLocalDataSource
import com.testlbc.data.repository.remote.SongJson
import com.testlbc.data.repository.remote.SongRemoteDataSource
import io.reactivex.Flowable
import io.reactivex.Single

interface SongRepository {

    fun synchronise(): Single<List<SongJson>>
    fun get(): Flowable<List<Song>>
}

class SongRepositoryImpl(
    private val remote: SongRemoteDataSource,
    private val local: SongLocalDataSource
) : SongRepository {

    override fun synchronise(): Single<List<SongJson>> {
        return remote.get().doOnSuccess { local.save(it) }
    }

    override fun get(): Flowable<List<Song>> {
        return local.get()
    }
}
