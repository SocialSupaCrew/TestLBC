package com.testlbc.data.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface SongDao {
    @Query("SELECT * FROM song ORDER BY albumId ASC, id ASC")
    fun getAll(): Flowable<List<Song>>

    @Query("SELECT * FROM song WHERE albumId = :albumId")
    fun getSongs(albumId: Int): Flowable<List<Song>>

    @Query("SELECT * FROM song WHERE id = :songId")
    fun getSong(songId: Int): Single<Song>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(audioFile: Song)
}
