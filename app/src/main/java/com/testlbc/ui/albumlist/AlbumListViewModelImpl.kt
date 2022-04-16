package com.testlbc.ui.albumlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.testlbc.core.EventPath
import com.testlbc.data.interactor.GetAlbumsInteractor
import timber.log.Timber

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

    override fun navigateTo(path: EventPath<Path>) {
        navigation.value = path
    }

    override fun fetchAlbums() {
        interactor.execute()
    }

    override fun onAlbumClicked(albumId: Int) {
        Timber.d("onAlbumClicked: $albumId")
    }

    override fun onCleared() {
        interactor.cleanUp()
        super.onCleared()
    }

    private fun onFetchAlbumsResult(result: GetAlbumsInteractor.Result) {
        when (result) {
            is GetAlbumsInteractor.Result.OnSuccess ->
                state.value = State.AlbumsLoaded(result.albums)
            GetAlbumsInteractor.Result.OnError -> state.value = State.ShowError
        }
    }
}
