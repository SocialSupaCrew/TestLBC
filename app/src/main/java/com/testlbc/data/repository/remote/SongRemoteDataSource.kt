package com.testlbc.data.repository.remote

import io.reactivex.Single

interface SongRemoteDataSource {
    fun get(): Single<List<SongJson>>
}
