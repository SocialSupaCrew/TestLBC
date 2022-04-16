package com.testlbc.data.repository.local

import com.testlbc.data.repository.remote.SongJson
import io.reactivex.Flowable

class SongLocalDataSourceImpl(private val database: AppDatabase) : SongLocalDataSource {

    override fun save(songs: List<SongJson>) {
        songs.map { toRoomEntity(it) }
            .forEach { database.songDao().insert(it) }
    }

    override fun get(): Flowable<List<Song>> {
        return database.songDao().getAll()
    }

    private fun toRoomEntity(song: SongJson): Song {
        return Song(song.id, song.albumId, song.title, song.url, song.thumbnailUrl)
    }
}
