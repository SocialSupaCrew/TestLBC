package com.testlbc.ui.albumdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.testlbc.core.EventPath
import com.testlbc.data.interactor.GetAlbumInteractor

class AlbumDetailViewModelImpl(
    private val state: MediatorLiveData<State>,
    private val navigation: MutableLiveData<EventPath<Path>>,
    private val interactor: GetAlbumInteractor,
    private val albumId: Int
) : AlbumDetailViewModel() {

    init {
        state.addSource(interactor.getLiveData(), ::onFetchSongsResult)
    }

    override fun getState(): LiveData<State> = state

    override fun getNavigation(): LiveData<EventPath<Path>> = navigation

    override fun navigateTo(path: Path) {
        navigation.value = path.toEventPath()
    }

    override fun fetchSongs() {
        interactor.execute(albumId)
    }

    override fun onSongClicked(songId: Int) {
        navigateTo(Path.SongDetail(songId))
    }

    private fun onFetchSongsResult(result: GetAlbumInteractor.Result) {
        state.value = when (result) {
            is GetAlbumInteractor.Result.OnSuccess -> State.SongsLoaded(result.songs)
            GetAlbumInteractor.Result.OnError -> State.ShowError
        }
    }
}
