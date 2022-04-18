package com.testlbc.ui.songdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.testlbc.core.EventPath
import com.testlbc.data.interactor.GetSongInteractor
import kotlinx.coroutines.launch

class SongDetailViewModelImpl(
    private val state: MediatorLiveData<State>,
    private val navigation: MutableLiveData<EventPath<Path>>,
    private val interactor: GetSongInteractor,
    private val songId: Int
) : SongDetailViewModel() {

    init {
        state.addSource(interactor.getLiveData(), ::onFetchSongResult)
    }

    override fun getState(): LiveData<State> = state

    override fun getNavigation(): LiveData<EventPath<Path>> = navigation

    override fun navigateTo(path: Path) {
        navigation.value = path.toEventPath()
    }

    override fun fetchSong() {
        viewModelScope.launch { interactor.execute(songId) }
    }


    private fun onFetchSongResult(result: GetSongInteractor.Result) {
        state.value = when (result) {
            is GetSongInteractor.Result.OnSuccess -> State.SongLoaded(result.song)
            GetSongInteractor.Result.OnError -> State.ShowError
        }
    }
}
