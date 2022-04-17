package com.testlbc.ui.albumdetail

import com.testlbc.core.BaseViewModel
import com.testlbc.core.EventPath
import com.testlbc.data.repository.local.Song
import com.testlbc.ui.albumdetail.AlbumDetailViewModel.Path
import com.testlbc.ui.albumdetail.AlbumDetailViewModel.State

abstract class AlbumDetailViewModel : BaseViewModel<State, Path>(), AlbumDetailHolder.Listener {

    sealed class State {
        data class SongsLoaded(val items: List<Song>) : State()
        object ShowError : State()
    }

    sealed class Path : EventPath.Path {
        data class SongDetail(val songId: Int) : Path()
    }

    abstract fun fetchSongs()
}
