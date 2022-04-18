package com.testlbc.ui.albumlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.testlbc.core.EventPath
import com.testlbc.data.interactor.GetAlbumsInteractor
import kotlinx.coroutines.launch

class AlbumListViewModelImpl(
    private val state: MediatorLiveData<State>,
    private val navigation: MutableLiveData<EventPath<Path>>,
    private val interactor: GetAlbumsInteractor
) : AlbumListViewModel() {

    init {
        state.addSource(interactor.getLiveData(), ::onFetchAlbumsResult)
    }

    override fun getState(): LiveData<State> = state

    override fun getNavigation(): LiveData<EventPath<Path>> = navigation

    override fun navigateTo(path: Path) {
        navigation.value = path.toEventPath()
    }

    override fun fetchAlbums() {
        viewModelScope.launch { interactor.execute() }
    }

    override fun onAlbumClicked(albumId: Int) {
        navigateTo(Path.AlbumDetail(albumId))
    }

    private fun onFetchAlbumsResult(result: GetAlbumsInteractor.Result) {
        state.value = when (result) {
            is GetAlbumsInteractor.Result.OnSuccess -> State.AlbumsLoaded(result.albums)
            GetAlbumsInteractor.Result.OnError -> State.ShowError
            GetAlbumsInteractor.Result.OnLoading -> State.ShowLoading
        }
    }
}
