package com.testlbc.data.repository.local

import com.testlbc.data.repository.remote.SongJson
import kotlinx.coroutines.flow.Flow

interface SongLocalDataSource {

    suspend fun save(songs: List<SongJson>)
    suspend fun get(): Flow<List<Song>>
    suspend fun getSongs(albumId: Int): Flow<List<Song>>
    suspend fun getSong(songId: Int): Song
}

class SongLocalDataSourceImpl(private val database: AppDatabase) : SongLocalDataSource {

    override suspend fun save(songs: List<SongJson>) {
        songs.map { toRoomEntity(it) }
            .forEach { database.songDao().insert(it) }
    }

    override suspend fun get(): Flow<List<Song>> {
        return database.songDao().getAll()
    }

    override suspend fun getSongs(albumId: Int): Flow<List<Song>> {
        return database.songDao().getSongs(albumId)
    }

    override suspend fun getSong(songId: Int): Song {
        return database.songDao().getSong(songId)
    }

    private fun toRoomEntity(song: SongJson): Song {
        return Song(song.id, song.albumId, song.title, song.url, song.thumbnailUrl)
    }
}
