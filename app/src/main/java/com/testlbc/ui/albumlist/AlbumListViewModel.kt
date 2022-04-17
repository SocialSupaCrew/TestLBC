package com.testlbc.ui.albumlist

import com.testlbc.core.BaseViewModel
import com.testlbc.core.EventPath
import com.testlbc.ui.albumlist.AlbumListViewModel.Path
import com.testlbc.ui.albumlist.AlbumListViewModel.State

abstract class AlbumListViewModel : BaseViewModel<State, Path>(), AlbumListHolder.Listener {

    sealed class State {
        data class AlbumsLoaded(val items: List<AlbumVM>) : State()
        object ShowLoading : State()
        object ShowError : State()
    }

    sealed class Path : EventPath.Path {
        data class AlbumDetail(val albumId: Int) : Path()
    }

    abstract fun fetchAlbums()

    data class AlbumVM(
        val id: Int,
        val name: String,
        val thumbnailUrl: String,
        val songCount: Int
    )
}
