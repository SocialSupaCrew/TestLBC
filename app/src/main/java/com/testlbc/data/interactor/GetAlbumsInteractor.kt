package com.testlbc.data.interactor

import com.testlbc.core.domain.BaseInteractor
import com.testlbc.core.domain.Interactor
import com.testlbc.data.interactor.GetAlbumsInteractor.Result
import com.testlbc.data.repository.SongRepository
import com.testlbc.ui.albumlist.AlbumListViewModel.AlbumVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import timber.log.Timber

interface GetAlbumsInteractor : Interactor<Result> {

    sealed class Result {
        data class OnSuccess(val albums: List<AlbumVM>) : Result()
        object OnError : Result()
        object OnLoading : Result()
    }

    suspend fun execute()
}

class GetAlbumsInteractorImpl(
    private val repository: SongRepository,
    private val mapper: AlbumMapper
) : BaseInteractor<Result>(), GetAlbumsInteractor {

    override suspend fun execute() {
        liveData.value = Result.OnLoading
        withContext(Dispatchers.Main) {
            try {
                repository.get().collect {
                    onSuccess(mapper.map(it))
                }
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    private fun onSuccess(albums: List<AlbumVM>) {
        liveData.value = Result.OnSuccess(albums)
    }

    private fun onError(throwable: Throwable) {
        Timber.e(throwable)
        liveData.value = Result.OnError
    }
}
