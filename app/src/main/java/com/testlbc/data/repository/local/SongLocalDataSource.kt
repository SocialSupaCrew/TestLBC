package com.testlbc.data.repository.local

import com.testlbc.data.repository.remote.SongJson
import io.reactivex.Flowable
import io.reactivex.Single

interface SongLocalDataSource {

    fun save(songs: List<SongJson>)
    fun get(): Flowable<List<Song>>
    fun getSongs(albumId: Int): Flowable<List<Song>>
    fun getSong(songId: Int): Single<Song>
}

class SongLocalDataSourceImpl(private val database: AppDatabase) : SongLocalDataSource {

    override fun save(songs: List<SongJson>) {
        songs.map { toRoomEntity(it) }
            .forEach { database.songDao().insert(it) }
    }

    override fun get(): Flowable<List<Song>> {
        return database.songDao().getAll()
    }

    override fun getSongs(albumId: Int): Flowable<List<Song>> {
        return database.songDao().getSongs(albumId)
    }

    override fun getSong(songId: Int): Single<Song> {
        return database.songDao().getSong(songId)
    }

    private fun toRoomEntity(song: SongJson): Song {
        return Song(song.id, song.albumId, song.title, song.url, song.thumbnailUrl)
    }
}
