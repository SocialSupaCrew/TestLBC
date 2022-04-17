package com.testlbc.data.interactor

import com.testlbc.data.repository.local.Song
import com.testlbc.ui.albumlist.AlbumListViewModel.AlbumVM

class AlbumMapper {

    fun map(songs: List<Song>): List<AlbumVM> {
        return songs.groupBy { it.albumId }
            .map {
                AlbumVM(
                    it.key,
                    "$ALBUM_NAME ${it.key}",
                    it.value.first().thumbnailUrl,
                    it.value.size
                )
            }
    }

    companion object {
        private const val ALBUM_NAME = "Album "
    }
}
