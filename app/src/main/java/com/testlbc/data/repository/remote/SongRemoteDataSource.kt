package com.testlbc.data.repository.remote

interface SongRemoteDataSource {
    suspend fun get(): List<SongJson>
}

class SongRemoteDataSourceImpl(private val service: SongService) : SongRemoteDataSource {

    override suspend fun get(): List<SongJson> {
        return service.get()
    }
}
