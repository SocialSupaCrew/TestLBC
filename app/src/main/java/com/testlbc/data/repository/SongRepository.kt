package com.testlbc.data.repository

import com.testlbc.data.repository.local.Song
import com.testlbc.data.repository.local.SongLocalDataSource
import com.testlbc.data.repository.remote.SongRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface SongRepository {

    suspend fun synchronise()
    suspend fun get(): Flow<List<Song>>
    suspend fun getSongs(albumId: Int): Flow<List<Song>>
    suspend fun getSong(songId: Int): Song
}

class SongRepositoryImpl(
    private val remote: SongRemoteDataSource,
    private val local: SongLocalDataSource
) : SongRepository {

    override suspend fun synchronise() {
        withContext(Dispatchers.IO) { local.save(remote.get()) }
    }

    override suspend fun get(): Flow<List<Song>> {
        return withContext(Dispatchers.IO) { local.get() }
    }

    override suspend fun getSongs(albumId: Int): Flow<List<Song>> {
        return withContext(Dispatchers.IO) { local.getSongs(albumId) }
    }

    override suspend fun getSong(songId: Int): Song {
        return withContext(Dispatchers.IO) { local.getSong(songId) }
    }
}
