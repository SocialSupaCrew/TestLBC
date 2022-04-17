package com.testlbc.ui.songdetail

import androidx.lifecycle.LiveData
import com.testlbc.core.BaseViewModel
import com.testlbc.core.EventPath
import com.testlbc.data.repository.local.Song
import com.testlbc.ui.songdetail.SongDetailViewModel.Path
import com.testlbc.ui.songdetail.SongDetailViewModel.State

abstract class SongDetailViewModel : BaseViewModel<State, Path>() {
    sealed class State {
        data class SongLoaded(val song: Song) : State()
        object ShowError : State()
    }

    sealed class Path : EventPath.Path {

    }

    abstract fun fetchSong()
}
