package com.testlbc.data.repository.remote

import io.reactivex.Single

class SongRemoteDataSourceImpl(private val service: SongService) : SongRemoteDataSource {

    override fun get(): Single<List<SongJson>> {
        return service.get()
    }
}
